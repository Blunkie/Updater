package org.example;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.reference.CtExecutableReference;
import spoon.support.reflect.code.CtFieldReadImpl;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main
{
	static CtPackage testPackage;
	static HashMap<String, Long> bufferMethodGarbageMap = new HashMap<>();
	public static void main(String[] args) throws IOException
	{
		String CLIENT_DOT_JAVA_SOURCE = "client.java";
		String METEOR_CLIENT_DEOB_SOURCE = "meteor-client-main";
		String MouseHandler_lastPressedTimeMillis;
		String clientMouseLastLastPressedTimeMillis;
		HashMap<Integer, String> menuActionMap = new HashMap<>();
		menuActionMap.put(2, "OPLOCT");
		menuActionMap.put(3, "OPLOC1");
		menuActionMap.put(4, "OPLOC2");
		menuActionMap.put(5, "OPLOC3");
		menuActionMap.put(6, "OPLOC4");
		menuActionMap.put(1001, "OPLOC5");
		menuActionMap.put(8, "OPNPCT");
		menuActionMap.put(9, "OPNPC1");
		menuActionMap.put(10, "OPNPC2");
		menuActionMap.put(11, "OPNPC3");
		menuActionMap.put(12, "OPNPC4");
		menuActionMap.put(13, "OPNPC5");
		menuActionMap.put(17, "OPOBJT");
		menuActionMap.put(18, "OPOBJ1");
		menuActionMap.put(19, "OPOBJ2");
		menuActionMap.put(20, "OPOBJ3");
		menuActionMap.put(21, "OPOBJ4");
		menuActionMap.put(22, "OPOBJ5");
		menuActionMap.put(15, "OPPLAYERT");
		menuActionMap.put(44, "OPPLAYER1");
		menuActionMap.put(45, "OPPLAYER2");
		menuActionMap.put(46, "OPPLAYER3");
		menuActionMap.put(47, "OPPLAYER4");
		menuActionMap.put(48, "OPPLAYER5");
		menuActionMap.put(49, "OPPLAYER6");
		menuActionMap.put(50, "OPPLAYER7");
		menuActionMap.put(51, "OPPLAYER8");
		menuActionMap.put(58, "IF_BUTTONT");

		HashMap<Integer, String> widgetActionMap = new HashMap<>();
		widgetActionMap.put(1, "IF_BUTTON1");
		widgetActionMap.put(2, "IF_BUTTON2");
		widgetActionMap.put(3, "IF_BUTTON3");
		widgetActionMap.put(4, "IF_BUTTON4");
		widgetActionMap.put(5, "IF_BUTTON5");
		widgetActionMap.put(6, "IF_BUTTON6");
		widgetActionMap.put(7, "IF_BUTTON7");
		widgetActionMap.put(8, "IF_BUTTON8");
		widgetActionMap.put(9, "IF_BUTTON9");
		widgetActionMap.put(10, "IF_BUTTON10");
		Launcher launcher = new Launcher();
		launcher.addInputResource(METEOR_CLIENT_DEOB_SOURCE);
		launcher.getEnvironment().setComplianceLevel(11);
		launcher.buildModel();
		CtModel model = launcher.getModel();
		List<ClientPacket> packetList = new ArrayList<>();
		testPackage = (CtPackage) model.getAllPackages().toArray()[0];



		CtMethod menuAction = testPackage.filterChildren(child -> child instanceof CtMethod && ((CtMethod<?>) child).getSimpleName().equals("menuAction")).first();
		System.out.println(menuAction.getParent());
		menuAction.filterChildren(child -> child instanceof CtIf).forEach(child ->
		{
			CtIf childIf = (CtIf) child;
			CtLiteral<?> condition =
					childIf.getCondition().filterChildren(child1 -> child1 instanceof CtLiteral).first();
			if (condition != null && childIf.getCondition().toString().contains("var") && !condition.toString().equals(
					"null") && menuActionMap.get(Integer.parseInt(condition.toString())) != null)
			{
				List<CtInvocation> invokes = childIf.getThenStatement().filterChildren(invocation -> invocation instanceof CtInvocation).list();
				packetList.add(parseInvokeList(invokes, menuActionMap.get(Integer.parseInt(condition.toString()))));
			}
		});



		CtMethod widgetMethod =
				testPackage.filterChildren(child -> child instanceof CtMethod && ((CtMethod<?>) child).getSimpleName().equals("widgetDefaultMenuAction")).first();
		widgetMethod.filterChildren(child -> child instanceof CtIf).forEach(child ->
		{
			CtIf childIf = (CtIf) child;
			CtLiteral<?> condition =
					childIf.getCondition().filterChildren(child1 -> child1 instanceof CtLiteral).first();
			if (condition != null && childIf.getCondition().toString().contains("var0") && childIf.getCondition().toString().contains("==") && !condition.toString().equals(
					"null") && widgetActionMap.get(Integer.parseInt(condition.toString())) != null)
			{
				List<CtInvocation> invokes = childIf.getThenStatement().filterChildren(invocation -> invocation instanceof CtInvocation).list();
				packetList.add(parseInvokeList(invokes, widgetActionMap.get(Integer.parseInt(condition.toString()))));
			}
		});


		CtIf clickIf =
				testPackage.filterChildren(child -> child instanceof CtIf && ((CtIf) child).getCondition().toString().contains("shouldProcessClick")).first();
		List<CtInvocation> clickInvokes =
				clickIf.getThenStatement().filterChildren(invocation -> invocation instanceof CtInvocation).list();
		packetList.add(parseInvokeList(clickInvokes, "EVENT_MOUSE_CLICK"));



		CtIf walkIf = testPackage.filterChildren(child -> child instanceof CtIf && ((CtIf) child).getCondition().toString().contains("shouldSendWalk")).first();
		List<CtInvocation> moveInvokes = walkIf.getThenStatement().filterChildren(invocation -> invocation instanceof CtInvocation).list();
		packetList.add(parseInvokeList(moveInvokes, "MOVE_GAMECLICK"));




		CtMethod resumePause = testPackage.filterChildren(child -> child instanceof CtMethod && ((CtMethod<?>) child).getSimpleName().equals("resumePauseWidget")).first();
		List<CtInvocation> resumePauseInvokes =
				resumePause.getBody().filterChildren(invocation -> invocation instanceof CtInvocation).list();
		packetList.add(parseInvokeList(resumePauseInvokes, "RESUME_PAUSEBUTTON"));



		CtField temp = testPackage.filterChildren(child -> child instanceof CtField && ((CtField<?>) child).getSimpleName().equals("mouseLastLastPressedTimeMillis")).first();
		clientMouseLastLastPressedTimeMillis =
				returnObfuscatedName(temp.getAnnotations());
		CtField temp2 = testPackage.filterChildren(child -> child instanceof CtField && ((CtField<?>) child).getSimpleName().equals("MouseHandler_lastPressedTimeMillis")).first();
		MouseHandler_lastPressedTimeMillis =
				returnObfuscatedName(temp2.getAssignment().getParent().getParent().getAnnotations()) +
						"." + returnObfuscatedName(temp2.getAnnotations());
		System.out.println("public static final String clientMouseLastLastPressedTimeMillis = \"" + clientMouseLastLastPressedTimeMillis + "\";");
		System.out.println("public static final String MouseHandler_lastPressedTimeMillisClass = \"" + MouseHandler_lastPressedTimeMillis.split("\\.")[0] + "\";");
		System.out.println("public static final String MouseHandler_lastPressedTimeMillisField = \"" + MouseHandler_lastPressedTimeMillis.split("\\.")[1] + "\";");
		List<String> clientFileLines = Files.readAllLines(Paths.get(CLIENT_DOT_JAVA_SOURCE));
		for (String line : clientFileLines)
		{
			if (line.contains(clientMouseLastLastPressedTimeMillis) && line.contains(MouseHandler_lastPressedTimeMillis) && line.contains("long var"))
			{
				System.out.println("public static final String MouseHandlerGarbage = \"" + line.split(" - ")[0].split("\\*")[1].replace(
						"L", "").trim() + "\";");
				System.out.println("public static final String ClientMouseHandlerGarbage= \"" + line.split(" - ")[1].split(
						"\\*")[1].replace("L", "").replace(";", "").trim() + "\";");
			}
			if (line.contains(clientMouseLastLastPressedTimeMillis + " = ") && line.contains(MouseHandler_lastPressedTimeMillis))
			{
				System.out.println("public static final String ClientMouseSetterGarbage = \"" + line.split("=")[1].split(
						"L")[0].trim() + "\";");
				break;
			}
		}
		for (ClientPacket clientPacket : packetList)
		{
			System.out.println(clientPacket.toString());
		}
		findRandomClassAndFieldNames();
		bufferMethodGarbageMap.forEach((k, v) -> System.out.println("put(\"" + k + "\",\"" + v + "\");"));
		System.out.println(packetList.size());
	}

	public static void findRandomClassAndFieldNames()
	{
		List<CtMethod> methods = testPackage.filterChildren(x -> x instanceof CtMethod).list();
		for (CtMethod method : methods)
		{
			if (method.getSimpleName().equals("getPacketBufferNode"))
			{
				CtClass classContainingGetPacketBufferNodeMethod = (CtClass) method.getParent();
				System.out.println("public static final String classContainingGetPacketBufferNodeName = \"" + returnObfuscatedName(classContainingGetPacketBufferNodeMethod.getAnnotations()) + "\";");
				System.out.println("public static final String getPacketBufferNodeMethodName = \"" + returnObfuscatedName(method.getAnnotations()) + "\";");
				System.out.println("public static final String getPacketBufferNodeGarbageValue = \"" + returnGarbageValue(method.getAnnotations()) + "\";");
			}
		}
		CtClass clientCtClass =
				testPackage.filterChildren(x -> x instanceof CtClass && ((CtClass<?>) x).getSimpleName().equals(
						"Client")).first();
		CtClass clientPacketCtClass =
				testPackage.filterChildren(x -> x instanceof CtClass && ((CtClass<?>) x).getSimpleName().equals(
						"ClientPacket")).first();
		System.out.println("public static final String clientPacketClassName = \"" + returnObfuscatedName(clientPacketCtClass.getAnnotations()) + "\";");
		CtClass bufferCtClass =
				testPackage.filterChildren(x -> x instanceof CtClass && ((CtClass<?>) x).getSimpleName().equals(
						"Buffer")).first();
		System.out.println("public static final String bufferClassName = \"" + returnObfuscatedName(bufferCtClass.getAnnotations()) + "\";");
		CtField packetWriterCtField = clientCtClass.filterChildren(x -> x instanceof CtField && ((CtField<?>) x).getSimpleName().equals(
				"packetWriter")).first();
		System.out.println("public static final String packetWriterFieldName = \"" + returnObfuscatedName(packetWriterCtField.getAnnotations()) + "\";");
		CtClass packetWriterCtClass =
				testPackage.filterChildren(x -> x instanceof CtClass && ((CtClass<?>) x).getSimpleName().equals(
						"PacketWriter")).first();
		System.out.println("public static final String packetWriterClassName = \"" + returnObfuscatedName(packetWriterCtClass.getAnnotations()) + "\";");
		CtMethod addNode = packetWriterCtClass.filterChildren(x -> x instanceof CtMethod && ((CtMethod<?>) x).getSimpleName().equals(
				"addNode")).first();
		System.out.println("public static final String addNodeMethodName = \"" + returnObfuscatedName(addNode.getAnnotations()) + "\";");
		System.out.println("public static final String addNodeGarbageValue = \"" + returnGarbageValue(addNode.getAnnotations()) + "\";");
		CtField isaacField = packetWriterCtClass.filterChildren(x -> x instanceof CtField && ((CtField<?>) x).getSimpleName().equals(
				"isaacCipher")).first();
		System.out.println("public static final String isaacCipherFieldName = \"" + returnObfuscatedName(isaacField.getAnnotations()) + "\";");
		CtClass packetBufferNodeClass = testPackage.filterChildren(x -> x instanceof CtClass && ((CtClass<?>) x).getSimpleName().equals(
				"PacketBufferNode")).first();
		System.out.println("public static final String packetBufferNodeClassName = \"" + returnObfuscatedName(packetBufferNodeClass.getAnnotations()) + "\";");
		CtField packetBuffer = packetBufferNodeClass.filterChildren(x -> x instanceof CtField && ((CtField<?>) x).getSimpleName().equals(
				"packetBuffer")).first();
		System.out.println("public static final String packetBufferFieldName = \"" + returnObfuscatedName(packetBuffer.getAnnotations()) + "\";");
	}

	public static String returnObfuscatedName(List<CtAnnotation<? extends Annotation>> annotations)
	{
		for (CtAnnotation annotation : annotations)
		{
			if (annotation.getName().equals("ObfuscatedName"))
			{
				return annotation.getValueAsString("value");
			}
		}
		return null;
	}

	public static String returnGarbageValue(List<CtAnnotation<? extends Annotation>> annotations)
	{
		for (CtAnnotation annotation : annotations)
		{
			if (annotation.getName().equals("ObfuscatedSignature"))
			{
				return annotation.getValueAsString("garbageValue");
			}
		}
		return null;
	}

	public static String[] printInvokeInfo(CtInvocation invoke)
	{
		String[] returnValues = new String[2];
		CtExecutableReference executableReference =
				invoke.filterChildren(child1 -> child1 instanceof CtExecutableReference).first();
		//System.out.println(invoke);
		for (CtAnnotation<? extends Annotation> annotation :
				executableReference.getExecutableDeclaration().getAnnotations())
		{
			if (annotation.getName().equals("ObfuscatedName"))
			{
				//System.out.println("obfuscated name: " + annotation.getValueAsString("value"));
				returnValues[0] = annotation.getValueAsString("value");
				continue;
			}
			if (annotation.getName().equals("ObfuscatedSignature"))
			{
				//System.out.println("garbage value: " + annotation.getValueAsString("garbageValue"));
				returnValues[1] = annotation.getValueAsString("garbageValue");
				continue;
			}
		}
		return returnValues;
	}

	public static String writeFormatter(String packetName, String write)
	{
		switch (packetName)
		{
			case "MOVE_GAMECLICK" ->
			{
				switch (write)
				{
					case String x && x.contains("baseX") ->
					{
						return "worldPointX";
					}
					case String x && x.contains("baseY") ->
					{
						return "worldPointY";
					}
					case String x && x.contains("(82)") ->
					{
						return "ctrlDown";
					}
					case String x && x.equals("5") ->
					{
						return "5";
					}
					default -> throw new UnsupportedOperationException("Unknown write: " + write);
				}
			}
			case "EVENT_MOUSE_CLICK" ->
			{
				switch (write)
				{
					case String x && x.contains("var4") ->
					{
						return "mouseX";
					}
					case String x && x.contains("var42") ->
					{
						return "mouseY";
					}
					case String x && x.contains("MouseHandler") ->
					{
						return "mouseInfo";
					}
					default -> throw new UnsupportedOperationException("Unknown write: " + write);
				}
			}
			case "OPLOCT", "OPOBJT" ->
			{
				switch (write)
				{
					case String x && x.contains("baseY") ->
					{
						return "worldPointY";
					}
					case String x && x.contains("selectedSpellItemId") ->
					{
						return "itemId";
					}
					case String x && x.contains("selectedSpellWidget") ->
					{
						return "widgetId";
					}
					case String x && x.contains("selectedSpellChildIndex") ->
					{
						return "slot";
					}
					case String x && x.contains("baseX") ->
					{
						return "worldPointX";
					}
					case String x && x.contains("(82)") ->
					{
						return "ctrlDown";
					}
					case String x && x.matches("var[0-9]") ->
					{
						return "objectId";
					}
					default -> throw new UnsupportedOperationException("Unknown write: " + write);
				}
			}
			case "OPNPCT" ->
			{
				switch (write)
				{
					case String x && x.contains("selectedSpellWidget") ->
					{
						return "widgetId";
					}
					case String x && x.contains("selectedSpellChildIndex") ->
					{
						return "slot";
					}
					case String x && x.contains("selectedSpellItemId") ->
					{
						return "itemId";
					}
					case String x && x.contains("(82)") ->
					{
						return "ctrlDown";
					}
					case String x && x.matches("var[0-9]") ->
					{
						return "npcIndex";
					}
					default -> throw new UnsupportedOperationException("Unknown write: " + write);
				}
			}
			case "OPPLAYERT" ->
			{
				switch (write)
				{
					case String x && x.contains("selectedSpellWidget") ->
					{
						return "widgetId";
					}
					case String x && x.contains("selectedSpellChildIndex") ->
					{
						return "slot";
					}
					case String x && x.contains("selectedSpellItemId") ->
					{
						return "itemId";
					}
					case String x && x.contains("(82)") ->
					{
						return "ctrlDown";
					}
					case String x && x.matches("var[0-9]") ->
					{
						return "playerIndex";
					}
					default -> throw new UnsupportedOperationException("Unknown write: " + write);
				}
			}
			case "IF_BUTTONT" ->
			{
				switch (write)
				{
					case String x && x.contains("var0") ->
					{
						return "destinationSlot";
					}
					case String x && x.contains("var4") ->
					{
						return "destinationItemId";
					}
					case String x && x.contains("var1") ->
					{
						return "destinationWidgetId";
					}
					case String x && x.contains("selectedSpellItemId") ->
					{
						return "sourceItemId";
					}
					case String x && x.contains("selectedSpellWidget") ->
					{
						return "sourceWidgetId";
					}
					case String x && x.contains("selectedSpellChildIndex") ->
					{
						return "sourceSlot";
					}
					default -> throw new UnsupportedOperationException("Unknown write: " + write);
				}
			}
			case "RESUME_PAUSEBUTTON" ->
			{
				switch (write)
				{
					case String x && x.contains("var0") ->
					{
						return "var0";
					}
					case String x && x.contains("var1") ->
					{
						return "var1";
					}
					default -> throw new UnsupportedOperationException("Unknown write: " + write);
				}
			}
			case "OPLOC1", "OPLOC2", "OPLOC3", "OPLOC4", "OPLOC5" ->
			{
				switch (write)
				{
					case String x && x.contains("baseY") ->
					{
						return "worldPointY";
					}
					case String x && x.contains("baseX") ->
					{
						return "worldPointX";
					}
					case String x && x.contains("(82)") ->
					{
						return "ctrlDown";
					}
					case String x && x.matches("var[0-9]") ->
					{
						return "objectId";
					}
					default -> throw new UnsupportedOperationException("Unknown write: " + write);
				}
			}
			case "OPNPC1", "OPNPC2", "OPNPC3", "OPNPC4", "OPNPC5" ->
			{
				switch (write)
				{
					case String x && x.contains("(82)") ->
					{
						return "ctrlDown";
					}
					case String x && x.matches("var[0-9]") ->
					{
						return "npcIndex";
					}
					default -> throw new UnsupportedOperationException("Unknown write: " + write);
				}
			}
			case "OPPLAYER1", "OPPLAYER2", "OPPLAYER3", "OPPLAYER4", "OPPLAYER5", "OPPLAYER6", "OPPLAYER7", "OPPLAYER8" ->
			{
				switch (write)
				{
					case String x && x.contains("(82)") ->
					{
						return "ctrlDown";
					}
					case String x && x.matches("var[0-9]") ->
					{
						return "playerIndex";
					}
					default -> throw new UnsupportedOperationException("Unknown write: " + write);
				}
			}
			//if_button 1-10
			case "IF_BUTTON1", "IF_BUTTON2", "IF_BUTTON3", "IF_BUTTON4", "IF_BUTTON5", "IF_BUTTON6", "IF_BUTTON7", "IF_BUTTON8", "IF_BUTTON9", "IF_BUTTON10" ->
			{
				switch (write)
				{
					case String x && x.contains("var1") ->
					{
						return "widgetId";
					}
					case String x && x.contains("var2") ->
					{
						return "slot";
					}
					case String x && x.contains("var3") ->
					{
						return "itemId";
					}
					default -> throw new UnsupportedOperationException("Unknown write: " + write);
				}
			}
			case "OPOBJ1", "OPOBJ2", "OPOBJ3", "OPOBJ4", "OPOBJ5" ->
			{
				switch (write)
				{
					case String x && x.contains("(82)") ->
					{
						return "ctrlDown";
					}
					case String x && x.contains("baseX") ->
					{
						return "worldPointX";
					}
					case String x && x.contains("baseY") ->
					{
						return "worldPointY";
					}
					case String x && x.matches("var[0-9]") ->
					{
						return "objectId";
					}
					default -> throw new UnsupportedOperationException("Unknown write: " + write);
				}
			}
		}
		return write;
	}
	public static ClientPacket parseInvokeList(List<CtInvocation> invokeList,String packetName)
	{
		ClientPacket packet = new ClientPacket();
		packet.name = packetName;
		for (
				CtInvocation invoke : invokeList)

		{
			if (invoke.getType().toString().contains("PacketBufferNode"))
			{
				CtFieldReadImpl clientPacket = (CtFieldReadImpl) invoke.getArguments().get(0);
				packet.obfuscatedName =
						clientPacket.getVariable().getDeclaration().getAnnotations().get(0).getValueAsString("value");
			}
			if (invoke.getTarget().getType().toString().contains("PacketBuffer"))
			{
				String[] output = printInvokeInfo(invoke);
				bufferMethodGarbageMap.put(output[0], Long.valueOf(output[1]));
				packet.writes.add(new String[]{output[0],
						writeFormatter(packet.name, invoke.getArguments().get(0).toString())});
			}
		}
		return packet;
	}
}