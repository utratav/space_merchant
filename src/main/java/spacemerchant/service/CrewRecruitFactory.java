package spacemerchant.service;

import spacemerchant.model.CrewMember;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class CrewRecruitFactory {
    private static final List<String> FIRST_NAMES = List.of(
            "Adam", "Adrian", "Alan", "Albert", "Aleksander", "Amelia", "Aneta", "Anita", "Anna", "Anton",
            "Ariel", "Artur", "Bartek", "Bartosz", "Borys", "Celina", "Cezary", "Daria", "Dawid", "Diana",
            "Dominik", "Dorota", "Edyta", "Eliza", "Emil", "Eryk", "Ewa", "Fabian", "Feliks", "Filip",
            "Gabriel", "Gaja", "Grzegorz", "Hanna", "Hubert", "Ida", "Igor", "Iga", "Ilona", "Irena",
            "Iwona", "Jacek", "Jakub", "Jan", "Janina", "Jaroslaw", "Jerzy", "Joanna", "Jowita", "Julia",
            "Julian", "Kacper", "Kaja", "Karol", "Katarzyna", "Kinga", "Klara", "Konrad", "Krystian", "Laura",
            "Lena", "Leon", "Lidia", "Lukasz", "Maciej", "Magda", "Maja", "Maks", "Marcel", "Marek",
            "Marta", "Mateusz", "Michal", "Milena", "Milosz", "Monika", "Natalia", "Nina", "Norbert", "Olaf",
            "Olga", "Oliwia", "Patryk", "Pawel", "Piotr", "Rafal", "Robert", "Roman", "Sandra", "Sebastian",
            "Sylwia", "Tomasz", "Wiktor", "Wiktoria", "Wojciech", "Zofia", "Zuzanna", "Nadia", "Nikodem", "Tymon"
    );

    private static final List<String> LAST_NAMES = List.of(
            "Adamski", "Anders", "Bajon", "Baran", "Bednarek", "Bialy", "Bielecki", "Borowski", "Brzoza", "Bukowski",
            "Chmiel", "Cichocki", "Cieslak", "Czajka", "Dabrowski", "Dobrowolski", "Domanski", "Duda", "Dudek", "Falkowski",
            "Filipowicz", "Gajda", "Glowacki", "Gorski", "Grabowski", "Grzelak", "Herman", "Jablonski", "Jakubowski", "Janicki",
            "Jankowski", "Jarosz", "Jaworski", "Kaleta", "Kamienski", "Kaminski", "Karczewski", "Kaszuba", "Kowal", "Kowalczyk",
            "Kowalski", "Krawczyk", "Krol", "Krupa", "Kubiak", "Kulesza", "Lewandowski", "Lis", "Maj", "Majewski",
            "Makowski", "Malinowski", "Mazur", "Michalak", "Michalski", "Mroz", "Nowak", "Nowicki", "Olszewski", "Orlowski",
            "Ostrowski", "Pawlak", "Pawlowski", "Piatek", "Piotrowski", "Polak", "Rutkowski", "Sawicki", "Sikora", "Sobczak",
            "Sokolowski", "Stasiak", "Stepien", "Szczepanski", "Szymanski", "Tomaszewski", "Urban", "Walczak", "Wasilewski", "Wilk",
            "Wisniewski", "Witkowski", "Wojcik", "Wolski", "Wozniak", "Wrona", "Wrobel", "Zajac", "Zalewski", "Zawadzki",
            "Zielinski", "Zuk", "Kozlowski", "Marciniak", "Czerwinski", "Kaczmarek", "Kozak", "Sadowski", "Szulc", "Laskowski"
    );

    private static final List<RoleTemplate> ROLE_TEMPLATES = List.of(
            new RoleTemplate("Pilot", 95, 55, 5, 2, 2, 2),
            new RoleTemplate("Nawigator", 90, 50, 4, 2, 3, 2),
            new RoleTemplate("Inzynier", 105, 60, 2, 2, 5, 1),
            new RoleTemplate("Mechanik", 110, 55, 2, 3, 4, 1),
            new RoleTemplate("Handlarz", 85, 65, 2, 1, 1, 5),
            new RoleTemplate("Negocjator", 85, 60, 2, 2, 1, 4),
            new RoleTemplate("Strzelec", 115, 60, 2, 5, 1, 1),
            new RoleTemplate("Ochroniarz", 125, 55, 1, 4, 2, 1),
            new RoleTemplate("Technik", 100, 55, 3, 2, 4, 1),
            new RoleTemplate("Medyk", 95, 50, 1, 2, 3, 3)
    );

    private final Random random;

    public CrewRecruitFactory() {
        this(new Random());
    }

    CrewRecruitFactory(Random random) {
        this.random = random;
    }

    public List<CrewMember> generateRecruitPool(int count) {
        if (count < 0 || count > FIRST_NAMES.size() * LAST_NAMES.size()) {
            throw new IllegalArgumentException("Nieprawidlowa liczba rekrutow do wygenerowania: " + count);
        }

        List<CrewMember> recruits = new ArrayList<>();
        Set<String> usedNames = new HashSet<>();

        while (recruits.size() < count) {
            String fullName = randomFullName();
            if (!usedNames.add(fullName)) {
                continue;
            }

            recruits.add(createRecruit(fullName));
        }

        return recruits;
    }

    static int getFirstNameCount() {
        return FIRST_NAMES.size();
    }

    static int getLastNameCount() {
        return LAST_NAMES.size();
    }

    private String randomFullName() {
        String firstName = FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size()));
        String lastName = LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));
        return firstName + " " + lastName;
    }

    private CrewMember createRecruit(String fullName) {
        RoleTemplate template = ROLE_TEMPLATES.get(random.nextInt(ROLE_TEMPLATES.size()));

        int maxHp = template.maxHp() + random.nextInt(21) - 10;
        int salary = template.salary() + random.nextInt(21) - 10;
        int piloting = tweakSkill(template.piloting());
        int combat = tweakSkill(template.combat());
        int engineering = tweakSkill(template.engineering());
        int trade = tweakSkill(template.trade());

        return new CrewMember(fullName, template.role(), maxHp, salary, piloting, combat, engineering, trade);
    }

    private int tweakSkill(int baseValue) {
        int value = baseValue + random.nextInt(3) - 1;
        return Math.max(1, Math.min(5, value));
    }

    private record RoleTemplate(
            String role,
            int maxHp,
            int salary,
            int piloting,
            int combat,
            int engineering,
            int trade
    ) {
    }
}
