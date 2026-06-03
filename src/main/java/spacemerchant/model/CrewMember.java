package spacemerchant.model;

public class CrewMember {
    private static final int MAX_SKILL = 5;
    private static final int EXPERIENCE_PER_SKILL_UP = 100;

    private String name;
    private String role;
    private int hp;
    private int maxHp;
    private int salary;
    private int piloting;
    private int combat;
    private int engineering;
    private int trade;
    private int pilotingExperience;
    private int combatExperience;
    private int engineeringExperience;
    private int tradeExperience;

    public CrewMember(String name, String role, int maxHp, int salary, int piloting, int combat, int engineering, int trade) {
        this.name = name;
        this.role = role;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.salary = salary;
        this.piloting = piloting;
        this.combat = combat;
        this.engineering = engineering;
        this.trade = trade;
        this.pilotingExperience = 0;
        this.combatExperience = 0;
        this.engineeringExperience = 0;
        this.tradeExperience = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        if (hp < 0) {
            this.hp = 0;
        } else if (hp > this.maxHp) {
            this.hp = this.maxHp;
        } else {
            this.hp = hp;
        }
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
        if (this.hp > this.maxHp) {
            this.hp = this.maxHp;
        }
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getPiloting() {
        return piloting;
    }

    public void setPiloting(int piloting) {
        this.piloting = clampSkill(piloting);
    }

    public int getCombat() {
        return combat;
    }

    public void setCombat(int combat) {
        this.combat = clampSkill(combat);
    }

    public int getEngineering() {
        return engineering;
    }

    public void setEngineering(int engineering) {
        this.engineering = clampSkill(engineering);
    }

    public int getTrade() {
        return trade;
    }

    public void setTrade(int trade) {
        this.trade = clampSkill(trade);
    }

    public int getExperience(CrewSkill skill) {
        return switch (skill) {
            case PILOTING -> pilotingExperience;
            case COMBAT -> combatExperience;
            case ENGINEERING -> engineeringExperience;
            case TRADE -> tradeExperience;
        };
    }

    public void setExperience(CrewSkill skill, int experience) {
        int safeExperience = Math.max(0, experience);
        switch (skill) {
            case PILOTING -> pilotingExperience = safeExperience;
            case COMBAT -> combatExperience = safeExperience;
            case ENGINEERING -> engineeringExperience = safeExperience;
            case TRADE -> tradeExperience = safeExperience;
        }
    }

    public void addExperience(CrewSkill skill, int amount) {
        if (amount <= 0 || getSkillValue(skill) >= MAX_SKILL) {
            return;
        }

        setExperience(skill, getExperience(skill) + amount);
        while (getExperience(skill) >= EXPERIENCE_PER_SKILL_UP && getSkillValue(skill) < MAX_SKILL) {
            setExperience(skill, getExperience(skill) - EXPERIENCE_PER_SKILL_UP);
            increaseSkill(skill);
        }
    }

    public void increaseSkill(CrewSkill skill) {
        switch (skill) {
            case PILOTING -> setPiloting(piloting + 1);
            case COMBAT -> setCombat(combat + 1);
            case ENGINEERING -> setEngineering(engineering + 1);
            case TRADE -> setTrade(trade + 1);
        }

        if (getSkillValue(skill) >= MAX_SKILL) {
            setExperience(skill, 0);
        }
    }

    public int getSkillValue(CrewSkill skill) {
        return switch (skill) {
            case PILOTING -> piloting;
            case COMBAT -> combat;
            case ENGINEERING -> engineering;
            case TRADE -> trade;
        };
    }

    public boolean canImprove(CrewSkill skill) {
        return getSkillValue(skill) < MAX_SKILL;
    }

    private int clampSkill(int value) {
        return Math.max(1, Math.min(MAX_SKILL, value));
    }
}