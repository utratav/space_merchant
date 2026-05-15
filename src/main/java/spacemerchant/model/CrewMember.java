package spacemerchant.model;

public class CrewMember {
    private String name;
    private String role;
    private int hp;
    private int maxHp;
    private int salary;
    private int piloting;
    private int combat;
    private int engineering;
    private int trade;

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
        this.piloting = piloting;
    }

    public int getCombat() {
        return combat;
    }

    public void setCombat(int combat) {
        this.combat = combat;
    }

    public int getEngineering() {
        return engineering;
    }

    public void setEngineering(int engineering) {
        this.engineering = engineering;
    }

    public int getTrade() {
        return trade;
    }

    public void setTrade(int trade) {
        this.trade = trade;
    }
}