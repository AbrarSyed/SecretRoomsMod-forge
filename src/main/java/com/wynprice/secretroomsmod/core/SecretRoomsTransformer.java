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
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.wynprice.secretroomsmod.render.FakeChunkCache;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
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
						if(mIns.getOpcode() == Opcodes.INVOKEVIRTUAL && (mIns.owner.equals("ChunkCacheOF") || mIns.owner.equals("net/optifine/override/ChunkCacheOF")/*Needed so Optifine works with SRM*/ || mIns.owner.equals("net/minecraft/world/ChunkCache")) && mIns.name.equals(getName("getBlockState", "func_180495_p")) && mIns.desc.equals("(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;")) {
							methodNode.instructions.set(ins, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/secretroomsmod/core/SecretRoomsHooksClient", "getBlockState", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", false));
							return;
						} 
					}
				}
			}
		}
	};
	
	/**
	 * <b>Transforms {@link BlockStateContainer.StateImplementation}</b>
	 * <br>Causes {@link IBlockProperties#doesSideBlockRendering(IBlockAccess, BlockPos, EnumFacing)} to be recieved through {@link SecretRoomsTransformer#doesSideBlockRendering(Block, IBlockState, IBlockAccess, BlockPos, EnumFacing)}.
	 * This means that the state can be set as something, yet if that block in the actual world is a SecretRoomsMod block, then the correct boolean will be used
	 * <br><br>
	 * Also transforms {@link IBlockProperties#addCollisionBoxToList(World, BlockPos, net.minecraft.util.math.AxisAlignedBB, java.util.List, net.minecraft.entity.Entity, boolean)} to be recieved through {@link SecretRoomsHooksClient#addCollisionBoxToList(Block, IBlockState, World, BlockPos, net.minecraft.util.math.AxisAlignedBB, java.util.List, net.minecraft.entity.Entity, boolean)}
	 * This meaans that if a block is being overridden with Energized Paste, it will have the correct collision box
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
				list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/secretroomsmod/core/SecretRoomsHooksClient", "doesSideBlockRendering", "(Lnet/minecraft/block/Block;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", false));
				list.add(new InsnNode(Opcodes.IRETURN));
				list.add(endLabel);
				methodNode.instructions = list;
			} else if(methodNode.name.equals(getName("shouldSideBeRendered", "func_185894_c"))) {
				for(int i = 0; i < methodNode.instructions.size(); i++) {
					AbstractInsnNode ins = methodNode.instructions.get(i);
					if(ins instanceof MethodInsnNode) {
						MethodInsnNode mIns = ((MethodInsnNode)ins);
						if(mIns.getOpcode() == Opcodes.INVOKEVIRTUAL && mIns.owner.equals("net/minecraft/block/Block") && mIns.name.equals(getName("shouldSideBeRendered", "func_176225_a")) && mIns.desc.equals("(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z")) {
							methodNode.instructions.set(ins, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/secretroomsmod/core/SecretRoomsHooksClient", "shouldSideBeRendered", "(Lnet/minecraft/block/Block;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", false));
						}
					}
				}
			} else if(methodNode.name.equals(getName("addCollisionBoxToList", "func_185908_a"))) {
				for(int i = 0; i < methodNode.instructions.size(); i++) {
					AbstractInsnNode ins = methodNode.instructions.get(i);
					if(ins instanceof MethodInsnNode) {
						MethodInsnNode mIns = ((MethodInsnNode)ins);
						if(mIns.getOpcode() == Opcodes.INVOKEVIRTUAL && mIns.owner.equals("net/minecraft/block/Block") && mIns.name.equals(getName("addCollisionBoxToList", "func_185477_a")) && mIns.desc.equals("(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;Z)V")) {
							methodNode.instructions.set(ins, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/secretroomsmod/core/SecretRoomsHooks", "addCollisionBoxToList", "(Lnet/minecraft/block/Block;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;Z)V", false));
						}
					}
				}
			}
		}
	};
	
	/**
	 * Transforms {@link BlockModelRenderer#renderModel(IBlockAccess, IBakedModel, IBlockState, BlockPos, net.minecraft.client.renderer.BufferBuilder, boolean, long)}, and sets the IBakedModel to be {@link SecretRoomsHooksClient#getActualModel(IBlockAccess, BlockPos, IBakedModel)}
	 */
	private final Consumer<ClassNode> BlockModelRenderer = (node) -> {
		for(MethodNode methodNode : node.methods) {
			if(methodNode.name.equals(getName("renderModel", "func_187493_a")) && methodNode.desc.equals("(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;ZJ)Z")) {
				methodNode.instructions.insert(new VarInsnNode(Opcodes.ASTORE, 2));
				methodNode.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/secretroomsmod/core/SecretRoomsHooksClient", "getActualModel", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/block/model/IBakedModel;)Lnet/minecraft/client/renderer/block/model/IBakedModel;", false));
				methodNode.instructions.insert(new VarInsnNode(Opcodes.ALOAD, 2));
				methodNode.instructions.insert(new VarInsnNode(Opcodes.ALOAD, 4));
				methodNode.instructions.insert(new VarInsnNode(Opcodes.ALOAD, 1));
				return;
			}
		}
	};
	
	/**
	 * Transforms {@link BlockRendererDispatcher#renderBlock(IBlockState, BlockPos, IBlockAccess, net.minecraft.client.renderer.BufferBuilder)}, and sets the IBlockState being used to be {@link SecretRoomsHooksClient#getActualState(IBlockAccess, BlockPos, IBlockState)}
	 */
	private final Consumer<ClassNode> BlockRendererDispatcher = (node) -> {
		for(MethodNode methodNode : node.methods) {
			if(methodNode.name.equals(getName("renderBlock", "func_175018_a")) && methodNode.desc.equals("(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/BufferBuilder;)Z")) {
				methodNode.instructions.insert(new VarInsnNode(Opcodes.ASTORE, 1));
				methodNode.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/secretroomsmod/core/SecretRoomsHooksClient", "getActualState", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)Lnet/minecraft/block/state/IBlockState;", false));
				methodNode.instructions.insert(new VarInsnNode(Opcodes.ALOAD, 1));
				methodNode.instructions.insert(new VarInsnNode(Opcodes.ALOAD, 2));
				methodNode.instructions.insert(new VarInsnNode(Opcodes.ALOAD, 3));
				return;
			}
		}
	};
	
	private final Consumer<ClassNode> BlockBreakable = (node) -> {
		for(MethodNode methodNode : node.methods) {
			if(methodNode.name.equals(getName("shouldSideBeRendered", "func_176225_a")) && methodNode.desc.equals("(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z")) {
				AbstractInsnNode startLabel = methodNode.instructions.getFirst();
				AbstractInsnNode lineNumberLabel = methodNode.instructions.get(1);
				AbstractInsnNode endLabel = methodNode.instructions.getLast();
				InsnList list = new InsnList();
				list.add(startLabel);
				list.add(lineNumberLabel);
				list.add(new VarInsnNode(Opcodes.ALOAD, 1));
				list.add(new VarInsnNode(Opcodes.ALOAD, 2));
				list.add(new VarInsnNode(Opcodes.ALOAD, 3));
				list.add(new VarInsnNode(Opcodes.ALOAD, 4));
				list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", getName("offset", "func_177972_a"), "(Lnet/minecraft/util/EnumFacing;)Lnet/minecraft/util/math/BlockPos;", false));
				list.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/world/IBlockAccess", getName("getBlockState", "func_180495_p"), "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", true));
				LabelNode label32 = new LabelNode();
 				list.add(new JumpInsnNode(Opcodes.IF_ACMPEQ, label32));
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(new VarInsnNode(Opcodes.ALOAD, 1));
				list.add(new VarInsnNode(Opcodes.ALOAD, 2));
				list.add(new VarInsnNode(Opcodes.ALOAD, 3));
				list.add(new VarInsnNode(Opcodes.ALOAD, 4));
				list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/block/Block", getName("shouldSideBeRendered", "func_176225_a"), "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", false));
				list.add(new JumpInsnNode(Opcodes.IFEQ, label32));
				list.add(new InsnNode(Opcodes.ICONST_1));
				LabelNode label33 = new LabelNode();
				list.add(new JumpInsnNode(Opcodes.GOTO, label33));
				list.add(label32);
				list.add(new InsnNode(Opcodes.ICONST_0));
				list.add(label33);
				list.add(new InsnNode(Opcodes.IRETURN));
				list.add(endLabel);
				methodNode.instructions = list;
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
		} else if(transformedName.equals("net.minecraft.block.BlockBreakable")) {
			basicClass = runConsumer(BlockBreakable, transformedName, basicClass);
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
	    byte[] abyte = writer.toByteArray();
		FMLLog.info("[SecretRoomsTransformer] Finished Transform on: " + transformedName);
	    return abyte;
	}
	
	/**
	 * Used to get the correct name for a method/field
	 * @param workspaceName if debof is not enabled, this will be returnedR
	 * @param mcpName if debof is enabled, this will be returned
	 */
	private String getName(String workspaceName, String mcpName) {
		return SecretRoomsCore.isDebofEnabled ? mcpName : workspaceName;
	}
	
}
