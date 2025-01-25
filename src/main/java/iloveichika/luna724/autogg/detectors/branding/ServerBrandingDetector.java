package iloveichika.luna724.autogg.detectors.branding;

import iloveichika.luna724.autogg.detectors.IDetector;
import iloveichika.luna724.autogg.handlers.patterns.PatternHandler;
import net.minecraft.client.Minecraft;

public class ServerBrandingDetector implements IDetector {
    @Override
    public boolean detect(String data) {
        return Minecraft.getMinecraft().thePlayer != null && PatternHandler.INSTANCE.getOrRegisterPattern(data).matcher(Minecraft.getMinecraft().thePlayer.getClientBrand()).matches();
    }
}
