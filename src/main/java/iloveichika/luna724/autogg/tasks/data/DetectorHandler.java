package iloveichika.luna724.autogg.tasks.data;

import iloveichika.luna724.autogg.detectors.IDetector;
import iloveichika.luna724.autogg.detectors.branding.ServerBrandingDetector;
import iloveichika.luna724.autogg.detectors.ip.ServerIPDetector;

public enum DetectorHandler {
    SERVER_BRANDING(new ServerBrandingDetector()),
    SERVER_IP(new ServerIPDetector());

    private final IDetector detector;

    DetectorHandler(IDetector detector) {
        this.detector = detector;
    }

    public IDetector getDetector() {
        return detector;
    }
}
