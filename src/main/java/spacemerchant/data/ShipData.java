package spacemerchant.data;

import spacemerchant.model.ShipModel;

import java.util.List;

public class ShipData {
    private static final ShipModel STARTER_SHUTTLE = new ShipModel(
            "starter_shuttle",
            "Prometeusz",
            "Lekki prom startowy. Tani w utrzymaniu, ale bez duzej ladowni.",
            0.0,
            100,
            100.0,
            50.0,
            4,
            1.0,
            List.of(
                    "              /--\\",
                    "             /    \\",
                    "            /      \\",
                    "           || {C1} ||",
                    "           ||      ||",
                    "          /| {C2}  |\\",
                    "         / |      | \\",
                    "         | | {H}  | |",
                    "         | | {L}  | |",
                    "         | | {F}  | |",
                    "         \\ | {C3} | /",
                    "          \\|      |/",
                    "           \\======/",
                    "            / || \\",
                    "              {C4}"
            )
    );

    private static final ShipModel CARGO_HAULER = new ShipModel(
            "cargo_hauler",
            "Atlas",
            "Ciezki frachtowiec z ogromna ladownia i mocnym kadlubem.",
            2200.0,
            160,
            140.0,
            180.0,
            5,
            1.4,
            List.of(
                    "             ___/\\___",
                    "            /        \\",
                    "      _____/  MOSTEK  \\_____",
                    "     /          {C1}         \\",
                    "    /========================\\",
                    "   || {C2}    CARGO    {C3} ||",
                    "   ||        [ {L} ]        ||",
                    "   ||                      ||",
                    "   ||   HULL {H}  FUEL {F} ||",
                    "   ||                      ||",
                    "   || {C4}            {C5} ||",
                    "    \\______________________/",
                    "        \\____ ENGINE ____/",
                    "             ||  ||",
                    "             ||  ||"
            )
    );

    private static final ShipModel BATTLE_CORVETTE = new ShipModel(
            "battle_corvette",
            "Szpon",
            "Korweta bojowa. Maly magazyn, ale twardy pancerz i wiecej koi.",
            3000.0,
            240,
            120.0,
            80.0,
            6,
            1.6,
            List.of(
                    "                 /\\",
                    "                /  \\",
                    "          _____/ {C1}\\_____",
                    "         /  ____    ____  \\",
                    "    ____/__/    \\__/    \\__\\____",
                    "   / {C2}     ARMOR {H}     {C3} \\",
                    "  |                              |",
                    "  |   BAY {L}      CORE      {F} |",
                    "  |                              |",
                    "   \\ {C4}     CREW DECK     {C5} /",
                    "    \\____                ____/",
                    "         \\____ {C6} ____/",
                    "              \\______/",
                    "             <||  ||>",
                    "             <||  ||>"
            )
    );

    private static final ShipModel DEEP_EXPLORER = new ShipModel(
            "deep_explorer",
            "Nomada",
            "Statek zwiadowczy dalekiego zasiegu z duzymi zbiornikami paliwa.",
            2600.0,
            130,
            260.0,
            90.0,
            5,
            0.8,
            List.of(
                    "                   /\\",
                    "                  /  \\",
                    "             ____/ {C1}\\____",
                    "            /              \\",
                    "       ____/  SENSOR ARRAY  \\____",
                    "      /                          \\",
                    "     | {C2}   FUEL {F}   {C3}    |",
                    "     |                            |",
                    "     |       HULL {H}             |",
                    "     |                            |",
                    "     |   CARGO {L}       {C4}     |",
                    "      \\                          /",
                    "       \\______ CREW {C5} ______/",
                    "              \\__DRIVE__/",
                    "                ||||||"
            )
    );

    private static final List<ShipModel> AVAILABLE_MODELS = List.of(
            STARTER_SHUTTLE,
            CARGO_HAULER,
            BATTLE_CORVETTE,
            DEEP_EXPLORER
    );

    private ShipData() {
    }

    public static ShipModel getStartingModel() {
        return STARTER_SHUTTLE;
    }

    public static List<ShipModel> getAvailableModels() {
        return AVAILABLE_MODELS;
    }
}
