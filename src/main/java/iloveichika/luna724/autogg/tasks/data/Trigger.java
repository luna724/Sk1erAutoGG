package iloveichika.luna724.autogg.tasks.data;

/**
 * Data holder for a trigger in the server triggers JsonArray
 * @author ChachyDev
 */
public class Trigger {
    private final int type;

    private final String pattern;

    private TriggerType triggerType;

    public Trigger(int type, String pattern) {
        this.type = type;
        this.pattern = pattern;
    }

    public TriggerType getType() {
        if (triggerType == null) triggerType = TriggerType.getByType(type);
        return triggerType;
    }

    public String getPattern() {
        return pattern;
    }
}
