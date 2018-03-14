package com.wynprice.secretroomsmod.core;

import java.util.function.Consumer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.render.FakeChunkCache;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
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
				for(int i = 0; i < methodNode.instructions.size(); i++) {
					AbstractInsnNode ins = methodNode.instructions.get(i);
					if(ins instanceof MethodInsnNode) {
						MethodInsnNode mIns = ((MethodInsnNode)ins);
						if(mIns.getOpcode() == Opcodes.INVOKEVIRTUAL && mIns.owner.equals("net/minecraft/block/Block") && mIns.name.equals("doesSideBlockRendering") && mIns.desc.equals("(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z")) {
							methodNode.instructions.set(ins, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/secretroomsmod/core/SecretRoomsTransformer", "doesSideBlockRendering", "(Lnet/minecraft/block/Block;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", false));
							return;
						}
					}
				}
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
			return world.getTileEntity(pos).getWorld().getBlockState(pos);
		}
		return world.getBlockState(pos);
	}
	
	/**
	 * @see #StateImplementation
	 * @param block Is not needed, however having this as the first param makes asm easier
	 */
	public static boolean doesSideBlockRendering(Block block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		if(world.getTileEntity(pos) instanceof ISecretTileEntity && ((ISecretBlock)world.getTileEntity(pos).getWorld().getBlockState(pos).getBlock()).getModelClass() != FakeBlockModel.class) {
			return false;
		}
		return block.doesSideBlockRendering(state, world, pos, face);
	}
	
}
