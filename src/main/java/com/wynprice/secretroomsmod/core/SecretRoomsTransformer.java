package com.wynprice.secretroomsmod.core;

import java.util.function.Consumer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.SecretCompatibility;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.render.FakeBlockAccess;
import com.wynprice.secretroomsmod.render.FakeChunkCache;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;
import com.wynprice.secretroomsmod.render.fakemodels.SecretBlockModel;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.FMLLog;

public class SecretRoomsTransformer implements IClassTransformer {
	
	
	/**
	 * Transforms {@link net.minecraft.client.renderer.chunk.RenderChunk}
	 * <br>Causes the iblockstate used to get the model to be recieved through {@link SecretRoomsTransformer#getBlockState(IBlockAccess, BlockPos)}. 
	 * This means that I am able to set up one state in the {@link FakeChunkCache}, and leave it, as the model is set elsewhere.
	 */
	private final Consumer<ClassNode> RenderChunk = (node) -> {
		for(MethodNode methodNode : node.methods) {
			if(methodNode.name.equals(getName("rebuildChunk", "func_178581_b"))) {
				for(int i = 0; i < methodNode.instructions.size(); i++) {
					AbstractInsnNode ins = methodNode.instructions.get(i);
					if(ins instanceof MethodInsnNode) {
						MethodInsnNode mIns = ((MethodInsnNode)ins);
						if(mIns.getOpcode() == Opcodes.INVOKEVIRTUAL && (mIns.owner.equals("ChunkCacheOF")/*Needed so Optifine works with SRM*/ || mIns.owner.equals("net/minecraft/world/ChunkCache")) && mIns.name.equals(getName("getBlockState", "func_180495_p")) && mIns.desc.equals("(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;")) {
							methodNode.instructions.set(ins, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/secretroomsmod/core/SecretRoomsTransformer", "getBlockState", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", false));
							return;
						} 
					}
				}
			}
		}
	};
	
	/**
	 * Transforms {@link BlockStateContainer.StateImplementation}
	 * <br>Causes {@link IBlockProperties#doesSideBlockRendering(IBlockAccess, BlockPos, EnumFacing)} to be recieved through {@link SecretRoomsTransformer#doesSideBlockRendering(Block, IBlockState, IBlockAccess, BlockPos, EnumFacing)}.
	 * This means that the state can be set as something, yet if that block in the actual world is a SecretRoomsMod block, then the correct boolean will be used
	 *
	 */
	private final Consumer<ClassNode> StateImplementation = (node) -> {
		for(MethodNode methodNode : node.methods) {
			if(methodNode.name.equals("doesSideBlockRendering")) {
				AbstractInsnNode startLabel = methodNode.instructions.getFirst();
				AbstractInsnNode lineNumberLabel = methodNode.instructions.get(1);
				AbstractInsnNode endLabel = methodNode.instructions.getLast();
				InsnList list = new InsnList();
				list.add(startLabel);
				list.add(lineNumberLabel);
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/block/state/BlockStateContainer$StateImplementation", getName("block", "field_177239_a"), "Lnet/minecraft/block/Block;"));
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(new VarInsnNode(Opcodes.ALOAD, 1));
				list.add(new VarInsnNode(Opcodes.ALOAD, 2));
				list.add(new VarInsnNode(Opcodes.ALOAD, 3));
				list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/secretroomsmod/core/SecretRoomsTransformer", "doesSideBlockRendering", "(Lnet/minecraft/block/Block;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", false));
				list.add(new InsnNode(Opcodes.IRETURN));
				list.add(endLabel);
			} else if(methodNode.name.equals(getName("shouldSideBeRendered", "func_185894_c"))) {
				for(int i = 0; i < methodNode.instructions.size(); i++) {
					AbstractInsnNode ins = methodNode.instructions.get(i);
					if(ins instanceof MethodInsnNode) {
						MethodInsnNode mIns = ((MethodInsnNode)ins);
						if(mIns.getOpcode() == Opcodes.INVOKEVIRTUAL && mIns.owner.equals("net/minecraft/block/Block") && mIns.name.equals(getName("shouldSideBeRendered", "func_176225_a")) && mIns.desc.equals("(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z")) {
							methodNode.instructions.set(ins, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/secretroomsmod/core/SecretRoomsTransformer", "shouldSideBeRendered", "(Lnet/minecraft/block/Block;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", false));
						}
					}
				}
			}
		}
	};
	
	private final Consumer<ClassNode> BlockModelRenderer = (node) -> {
		for(MethodNode methodNode : node.methods) {
			if(methodNode.name.equals(getName("renderModel", "func_187493_a")) && methodNode.desc.equals("(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;ZJ)Z")) {
				methodNode.instructions.insert(new VarInsnNode(Opcodes.ASTORE, 2));
				methodNode.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/secretroomsmod/core/SecretRoomsTransformer", "getActualModel", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/block/model/IBakedModel;)Lnet/minecraft/client/renderer/block/model/IBakedModel;", false));
				methodNode.instructions.insert(new VarInsnNode(Opcodes.ALOAD, 2));
				methodNode.instructions.insert(new VarInsnNode(Opcodes.ALOAD, 4));
				methodNode.instructions.insert(new VarInsnNode(Opcodes.ALOAD, 1));
				return;
			}
		}
	};
	
	private final Consumer<ClassNode> BlockRendererDispatcher = (node) -> {
		for(MethodNode methodNode : node.methods) {
			if(methodNode.name.equals(getName("renderBlock", "func_175018_a")) && methodNode.desc.equals("(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/BufferBuilder;)Z")) {
				methodNode.instructions.insert(new VarInsnNode(Opcodes.ASTORE, 1));
				methodNode.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/secretroomsmod/core/SecretRoomsTransformer", "getActualState", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)Lnet/minecraft/block/state/IBlockState;", false));
				methodNode.instructions.insert(new VarInsnNode(Opcodes.ALOAD, 1));
				methodNode.instructions.insert(new VarInsnNode(Opcodes.ALOAD, 2));
				methodNode.instructions.insert(new VarInsnNode(Opcodes.ALOAD, 3));
				return;
			}
		}
	};
	
	public SecretRoomsTransformer() {
		FMLLog.info("[SecretRoomsTransformer] Registered");
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(transformedName.equals("net.minecraft.client.renderer.chunk.RenderChunk")) {
			basicClass = runConsumer(RenderChunk, transformedName, basicClass);
		} else if(transformedName.equals("net.minecraft.block.state.BlockStateContainer$StateImplementation")) {
			basicClass = runConsumer(StateImplementation, transformedName, basicClass);
		} else if(transformedName.equals("net.minecraft.client.renderer.BlockModelRenderer")) {
			basicClass = runConsumer(BlockModelRenderer, transformedName, basicClass);
		} else if(transformedName.equals("net.minecraft.client.renderer.BlockRendererDispatcher")) {
			basicClass = runConsumer(BlockRendererDispatcher, transformedName, basicClass);
		}
		return basicClass;
		
	}
	
	private byte[] runConsumer(Consumer<ClassNode> cons, String transformedName, byte[] basicClass) {			
		FMLLog.info("[SecretRoomsTransformer] Running Transform on " + transformedName);
		ClassNode node = new ClassNode();
	    ClassReader reader = new ClassReader(basicClass);
	    reader.accept(node, 0);
	    
		cons.accept(node);
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
	    node.accept(writer);
	    return writer.toByteArray();
	}
	
	/**
	 * Used to get the correct name for a method/field
	 * @param workspaceName if debof is not enabled, this will be returnedR
	 * @param mcpName if debof is enabled, this will be returned
	 */
	private String getName(String workspaceName, String mcpName) {
		return SecretRoomsCore.isDebofEnabled ? mcpName : workspaceName;
	}
	
	/**
	 * @see #RenderChunk
	 */
	public static IBlockState getBlockState(IBlockAccess world, BlockPos pos) {
		if(world.getTileEntity(pos) instanceof ISecretTileEntity) {
			IBlockState state = world.getTileEntity(pos).getWorld().getBlockState(pos);
			if(isMalisisDoor(state)) {
				return world.getBlockState(pos);
			}
			IBlockState renderState = ((ISecretTileEntity)world.getTileEntity(pos)).getMirrorState();
			try
			{
				renderState = renderState.getActualState(new FakeBlockAccess(world.getTileEntity(pos).getWorld()), pos);
				state = state.getActualState(world, pos);
			}
			catch (Exception e) 
			{
				;
			}
			state = ((IExtendedBlockState)state).withProperty(ISecretBlock.RENDER_PROPERTY, renderState);
			SecretBlockModel.instance().AO.set(Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(renderState).isAmbientOcclusion());
			SecretBlockModel.instance().SRMBLOCK.set(state);
			return state;
		}
		return world.getBlockState(pos);
	}
	
	/**
	 * @see #StateImplementation
	 * @param block Is not needed, however having this as the first param makes asm easier
	 */
	public static boolean doesSideBlockRendering(Block block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		if(!isMalisisDoor(state) && world.getTileEntity(pos) instanceof ISecretTileEntity && ((ISecretBlock)world.getTileEntity(pos).getWorld().getBlockState(pos).getBlock()).getModelClass() != FakeBlockModel.class) {
			return false;
		}
		return block.doesSideBlockRendering(state, world, pos, face);
	}
	
	public static boolean shouldSideBeRendered(Block block, IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing face) {
		if(!isMalisisDoor(state) && access.getTileEntity(pos) instanceof ISecretTileEntity) {
			World world = access.getTileEntity(pos).getWorld(); //Need to use world as the IBlockAccess wont return the right states
			IBlockState blockState = world.getTileEntity(pos).getWorld().getBlockState(pos);
			return blockState.getBlock().shouldSideBeRendered(blockState, world, pos, face);
		}
		return block.shouldSideBeRendered(state, access, pos, face);
	}
	
	public static IBakedModel getActualModel(IBlockAccess access, BlockPos pos, IBakedModel model) {
		if(access.getTileEntity(pos) instanceof ISecretTileEntity) {
			IBlockState state = access.getTileEntity(pos).getWorld().getBlockState(pos);
			if(isMalisisDoor(state)) {
				return model;
			}
			return SecretBlockModel.instance();
		}
		return model;
	}
	
	public static IBlockState getActualState(IBlockAccess access, BlockPos pos, IBlockState state) {
		if(isMalisisDoor(state)) {
			return state;
		}
		
		if(state.getBlock() instanceof ISecretBlock) {
			IBlockState mirroredState = access.getBlockState(pos);
			try {
				mirroredState = mirroredState.getActualState(access, pos);
			} catch (Exception e) {
				;
			}
			return mirroredState;
		}
		return state;
	}
	
	private static boolean isMalisisDoor(IBlockState state) {
		return SecretCompatibility.MALISISDOORS && (state.getBlock() == SecretBlocks.SECRET_WOODEN_DOOR || state.getBlock() == SecretBlocks.SECRET_IRON_DOOR);
	}
	
}
