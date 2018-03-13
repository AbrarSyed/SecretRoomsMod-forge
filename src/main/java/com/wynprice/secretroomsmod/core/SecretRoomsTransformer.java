package com.wynprice.secretroomsmod.core;

import java.lang.reflect.Proxy;
import java.util.function.Consumer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.FMLLog;

public class SecretRoomsTransformer implements IClassTransformer {
	
	
	private final Consumer<ClassNode> RenderChunk = (node) -> {
		for(MethodNode methodNode : node.methods) {
			if(methodNode.name.equals(getName("rebuildChunk", "func_178581_b "))) {
				for(int i = 0; i < methodNode.instructions.size(); i++) {
					AbstractInsnNode ins = methodNode.instructions.get(i);
					if(ins instanceof MethodInsnNode) {
						MethodInsnNode mIns = ((MethodInsnNode)ins);
						if(mIns.getOpcode() == Opcodes.INVOKEVIRTUAL && mIns.owner.equals("net/minecraft/world/ChunkCache") && mIns.name.equals(getName("getBlockState", "func_180495_p")) && mIns.desc.equals("(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;")) {
							methodNode.instructions.set(ins, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/secretroomsmod/core/SecretRoomsTransformer", "getBlockState", "(Lnet/minecraft/world/ChunkCache;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", false));
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
	
	private String getName(String workspaceName, String mcpName) {
		return SecretRoomsCore.isDebofEnabled ? mcpName : workspaceName;
	}
	
	public static IBlockState getBlockState(ChunkCache cache, BlockPos pos) {
		if(cache.getTileEntity(pos) instanceof ISecretTileEntity) {
			return cache.getTileEntity(pos).getWorld().getBlockState(pos);
		}
		return cache.getBlockState(pos);
	}
	
}
