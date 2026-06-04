package spacemerchant.model;

public enum CrewSkill {
    PILOTING("Pilotaż"),
    COMBAT("Walka"),
    ENGINEERING("Inżynieria"),
    TRADE("Handel");

    private final String displayName;

    CrewSkill(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
