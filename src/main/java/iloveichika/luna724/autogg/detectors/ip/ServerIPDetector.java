package iloveichika.luna724.autogg.detectors.ip;

import iloveichika.luna724.autogg.detectors.IDetector;
import iloveichika.luna724.autogg.handlers.patterns.PatternHandler;
import net.minecraft.client.Minecraft;

public class ServerIPDetector implements IDetector {
    @Override
    public boolean detect(String data) {
        return Minecraft.getMinecraft().thePlayer != null && PatternHandler.INSTANCE.getOrRegisterPattern(data).matcher(Minecraft.getMinecraft().getCurrentServerData().serverIP).matches();
    }
}
