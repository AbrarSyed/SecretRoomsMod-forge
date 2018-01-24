package com.wynprice.secretroomsmod.core;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLLog;

public class UVTransformer implements IClassTransformer
{
	
	public UVTransformer() {
        FMLLog.info("[UVTransformer] Initialized.");
    }

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) 
	{
		if(!transformedName.equals("net.minecraft.client.renderer.block.model.FaceBakery"))
            return basicClass;
		String methodStoreVertexName = SRMCore.isDebofEnabled ? "func_178404_a" : "storeVertexData";
        String methodStoreVertexDesc = "([IIILorg/lwjgl/util/vector/Vector3f;ILnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/renderer/block/model/BlockFaceUV;)V";
		
		
		ClassNode node = new ClassNode();
	    ClassReader reader = new ClassReader(basicClass);
	    reader.accept(node, 0);
	    
	    MethodNode storeVertexData = new MethodNode(Opcodes.ACC_PUBLIC, methodStoreVertexName, methodStoreVertexDesc, null, new String[0]);
	    LabelNode startLabel = new LabelNode();
        LabelNode endLabel = new LabelNode();
        storeVertexData.instructions = new InsnList();
        storeVertexData.instructions.add(startLabel);
        storeVertexData.instructions.add(new LineNumberNode(152, startLabel)); //Required
        
        LocalVariableNode faceData = new LocalVariableNode(SRMCore.isDebofEnabled ? "p_178404_1_" : "faceData", "[I", null, startLabel, endLabel, 0);
        LocalVariableNode storeIndex = new LocalVariableNode(SRMCore.isDebofEnabled ? "p_178404_2_" :"storeIndex", "I", null, startLabel, endLabel, 1);
        LocalVariableNode vertexIndex = new LocalVariableNode(SRMCore.isDebofEnabled ? "p_178404_3_" : "vertexIndex", "I", null, startLabel, endLabel, 2);
        LocalVariableNode position = new LocalVariableNode(SRMCore.isDebofEnabled ? "p_178404_4_" : "position", "Lorg/lwjgl/util/vector/Vector3f;", null, startLabel, endLabel, 3);
        LocalVariableNode shadeColor = new LocalVariableNode(SRMCore.isDebofEnabled ? "p_178404_5_" : "shadeColor", "I", null, startLabel, endLabel, 4);
        LocalVariableNode sprite = new LocalVariableNode(SRMCore.isDebofEnabled ? "p_178404_6_" : "sprite", "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", null, startLabel, endLabel, 5);
        LocalVariableNode faceUV = new LocalVariableNode(SRMCore.isDebofEnabled ? "p_178404_7_" : "faceUV", "Lnet/minecraft/client/renderer/block/model/BlockFaceUV;", null, startLabel, endLabel, 6);
        storeVertexData.localVariables = Lists.newArrayList(faceData, storeIndex, vertexIndex, position, shadeColor, sprite, faceUV);
        
        storeVertexData.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        storeVertexData.instructions.add(new VarInsnNode(Opcodes.ILOAD, 2));
        storeVertexData.instructions.add(new VarInsnNode(Opcodes.ILOAD, 3));
        storeVertexData.instructions.add(new VarInsnNode(Opcodes.ALOAD, 4));
        storeVertexData.instructions.add(new VarInsnNode(Opcodes.ILOAD, 5));
        storeVertexData.instructions.add(new VarInsnNode(Opcodes.ALOAD, 6));
        storeVertexData.instructions.add(new VarInsnNode(Opcodes.ALOAD, 7));
        
        storeVertexData.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/wynprice/secretroomsmod/core/UVTransformer", "storeVertexData", "([IIILorg/lwjgl/util/vector/Vector3f;ILnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/renderer/block/model/BlockFaceUV;)V", false));
        storeVertexData.instructions.add(new InsnNode(Opcodes.RETURN));
        storeVertexData.instructions.add(endLabel);
        
        ArrayList<MethodNode> nodes = new ArrayList<>();
        for(MethodNode m : node.methods)
        	if(!m.desc.equalsIgnoreCase(methodStoreVertexDesc) || !m.name.equalsIgnoreCase(methodStoreVertexName))
        		nodes.add(m);
        
        nodes.add(storeVertexData);
        
        node.methods.clear();
        node.methods.addAll(nodes);
        
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        node.accept(writer);
        basicClass = writer.toByteArray();
		return basicClass;
	}
	
	public static final HashMap<int[], BlockFaceUV> BLOCKUV_DATA = new HashMap<>();
	
	public static void storeVertexData(int[] faceData, int storeIndex, int vertexIndex, Vector3f position, int shadeColor, TextureAtlasSprite sprite, BlockFaceUV faceUV)
    {
		int i = storeIndex * (faceData.length / 4);
        faceData[i] = Float.floatToRawIntBits(position.x);
        faceData[i + 1] = Float.floatToRawIntBits(position.y);
        faceData[i + 2] = Float.floatToRawIntBits(position.z);
        faceData[i + 3] = shadeColor;
        faceData[i + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU((double)faceUV.getVertexU(vertexIndex) * .999 + faceUV.getVertexU((vertexIndex + 2) % 4) * .001));
        faceData[i + 4 + 1] = Float.floatToRawIntBits(sprite.getInterpolatedV((double)faceUV.getVertexV(vertexIndex) * .999 + faceUV.getVertexV((vertexIndex + 2) % 4) * .001));
        BLOCKUV_DATA.put(faceData, faceUV);        	
    }
	
	public static BlockFaceUV getUV(int[] list, int pos)
	{
		return BLOCKUV_DATA.containsKey(list) ? BLOCKUV_DATA.get(list) : null;
	}
	
	

}
