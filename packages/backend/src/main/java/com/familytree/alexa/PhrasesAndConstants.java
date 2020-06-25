package com.familytree.alexa;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class PhrasesAndConstants {

    private PhrasesAndConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String CARD_TITLE = "Familienstammbaum";
    public static final String COLOR_KEY = "COLOR";
    public static final String COLOR_SLOT = "Color";
    public static final String MEMBER_KEY = "Member";
    public static final String MEMBER_SLOT = "Member";
    public static final String MEMBER_SECOND_KEY = "MemberS";
    public static final String MEMBER_SECOND_SLOT = "MemberS";
    public static final String NAME_SLOT = "Name";

    public static final String WELCOME = "Hallo. Ich kenne deinen Familienstammbaum. Bitte sage mir zum Beispiel: Wie heißt mein Cousin.";
    public static final String HELP = "Du kannst mir Deine Lieblingsfarbe sagen. Sage zum Beispiel: Meine Lieblingsfarbe ist gelb.";
    public static final String HELP_REPROMPT = "Bitte sage mir Deine Lieblingsfarbe.";
    public static final String CANCEL_AND_STOP = "Auf Wiedersehen";
    public static final String FALLBACK = "Tut mir leid, das weiss ich nicht. Sage einfach Hilfe.";
    public static final String SAY_LIEBLINGAFARBE_REPROMPT = "Das habe ich nicht verstanden. Bitte sage mir Deine Lieblingsfarbe.";
    public static final String LIEBLINGSFARBE_IS = "Deine Lieblingsfarbe ist ";
    public static final String WHAT_IS_LIEBLINGSFARBE = "Du kannst mich jetzt nach deiner Lieblingsfarbe fragen. Frage einfach: Was ist meine Lieblingsfarbe?";
    public static final String LIEBLINGSFABE_UNKNOWN = "Ich kenne Deine Lieblingsfarbe nicht. Sag mir Deine Lieblingsfarbe. Sage zum Beispiel: Ich mag rot.";
    public static final String REPROMPT_LIEBINGSFARBE = "Welche Farbe magst Du am liebsten?";

    public static final String CHANGE_LIEBLINGSFARBE = "Wenn Du deine Lieblingsfarbe aendern willst, dann sage mir Deine neue Lieblingsfarbe.";

    // FamilyTree
    public static final String NAME_IS = "Der Name, nach dem du suchst ist";
    public static final String GOOD_BYE = "Auf Wiedersehen";
    public static final String YOUR_BROTHER = "dein Bruder";
    public static final String YOUR_SISTER = "deine Schwester";
    public static final String YOUR_MALE_COUSIN = "dein Cousin";
    public static final String YOUR_FEMALE_COUSIN = "deine Cousine";
    public static final String YOUR_GRANDSON = "dein Enkel";
    public static final String YOUR_GRANDDAUGHTER = "deine Enkelin";
    public static final String HAS_ON = "hat am";
    public static final String IS = "ist";
    public static final String YEARS_OLD = "Jahre alt";
    public static final String BIRTHDAY = "Geburtstag";
    public static final String BORN = "geboren. Das macht";
    public static final String HIM = "ihn";
    public static final String HER = "sie";
    public static final String PHONENUMBER = "hat die Telefonnummer";
    public static final String WORK = "arbeitet als";
    public static final String HOBBY_OUTPUT = "betreibt das Hobby";
    public static final String NO_HOBBIES_FOUND = "hat keine Hobbies";
    public static final String HOBBY_OUTPUT_MULTIPLE = "hat folgende Hobbies:";

    public static final String NO_ALEXA_MATCH = "Ich habe wohl die Anfrage falsch verstanden.";
    public static final String NO_DB_MATCH = "Ich habe niemand passenden in der Datenbank gefunden.";
    public static final String MULTIPLE_MATCHES = "Folgende Personen habe ich gefunden: ";

    private static final String YOURS_FEMALE = "deine";
    private static final String YOURS_MALE = "dein";

    public enum RELATIONS {
        GRANDFATHER(YOURS_MALE, "Großvater"), GRANDMOTHER(YOURS_FEMALE, "Großmutter"), NEPHEW(YOURS_MALE, "Neffe"),
        NIECE(YOURS_FEMALE, "Nichte"), SISINLAW(YOURS_FEMALE, "Schwägerin"), BROINLAW(YOURS_MALE, "Schwager"),
        M_COUSIN(YOURS_MALE, "Cousin"), F_COUSIN(YOURS_FEMALE, "Cousine"), SON(YOURS_MALE, "Sohn"),
        DAUGHTER(YOURS_FEMALE, "Tochter"), GRANDSON(YOURS_MALE, "Enkel"), GRANDDAUGHTER(YOURS_FEMALE, "Enkelin"),
        MOTHER(YOURS_FEMALE, "Mutter"), FATHER(YOURS_MALE, "Vater"), BROTHER(YOURS_MALE, "Bruder"),
        UNCLE(YOURS_MALE, "Onkel"), AUNT(YOURS_MALE, "Tante"), SISTER(YOURS_FEMALE, "Schwester");

        private static Map<RELATIONS, Map<RELATIONS, RELATIONS[]>> FAMILYRELATIONS = new HashMap<RELATIONS, Map<RELATIONS, RELATIONS[]>>();

        private String possesivpronomen;
        private String relation;

        public String getPossesivpronomen() {
            return possesivpronomen;
        }

        public String getRelation() {
            return relation;
        }

        private static void fillRelations() {
            // GRANDFATHER
            RELATIONS r = RELATIONS.GRANDFATHER;
            Map<RELATIONS, RELATIONS[]> map = new HashMap<RELATIONS, RELATIONS[]>();
            map.put(RELATIONS.SON, new RELATIONS[] { RELATIONS.FATHER, RELATIONS.UNCLE });
            map.put(RELATIONS.DAUGHTER, new RELATIONS[] { RELATIONS.MOTHER, RELATIONS.AUNT });
            FAMILYRELATIONS.put(r, map);

            // GRANDMOTHER
            r = RELATIONS.GRANDMOTHER;
            map = new HashMap<RELATIONS, RELATIONS[]>();
            map.put(RELATIONS.SON, new RELATIONS[] { RELATIONS.FATHER, RELATIONS.UNCLE });
            map.put(RELATIONS.DAUGHTER, new RELATIONS[] { RELATIONS.MOTHER, RELATIONS.AUNT });
            FAMILYRELATIONS.put(r, map);

            // NEPHEW
            r = RELATIONS.NEPHEW;
            map = new HashMap<RELATIONS, RELATIONS[]>();
            map.put(RELATIONS.FATHER, new RELATIONS[] { RELATIONS.BROTHER, RELATIONS.BROINLAW });
            map.put(RELATIONS.MOTHER, new RELATIONS[] { RELATIONS.SISTER, RELATIONS.SISINLAW });
            map.put(RELATIONS.BROTHER, new RELATIONS[] { RELATIONS.NEPHEW });
            map.put(RELATIONS.SISTER, new RELATIONS[] { RELATIONS.NIECE });
            FAMILYRELATIONS.put(r, map);

            // NIECE
            r = RELATIONS.NIECE;
            map = new HashMap<RELATIONS, RELATIONS[]>();
            map.put(RELATIONS.FATHER, new RELATIONS[] { RELATIONS.BROTHER, RELATIONS.BROINLAW });
            map.put(RELATIONS.MOTHER, new RELATIONS[] { RELATIONS.SISTER, RELATIONS.SISINLAW });
            map.put(RELATIONS.BROTHER, new RELATIONS[] { RELATIONS.NEPHEW });
            map.put(RELATIONS.SISTER, new RELATIONS[] { RELATIONS.NIECE });
            FAMILYRELATIONS.put(r, map);

            // M_COUSIN
            r = RELATIONS.M_COUSIN;
            map = new HashMap<RELATIONS, RELATIONS[]>();
            map.put(RELATIONS.FATHER, new RELATIONS[] { RELATIONS.UNCLE });
            map.put(RELATIONS.MOTHER, new RELATIONS[] { RELATIONS.AUNT });
            map.put(RELATIONS.FATHER, new RELATIONS[] { RELATIONS.BROTHER, RELATIONS.BROINLAW });
            FAMILYRELATIONS.put(r, map);

            // FATHER
            r = RELATIONS.FATHER;
            map = new HashMap<RELATIONS, RELATIONS[]>();
            map.put(RELATIONS.BROTHER, new RELATIONS[] { RELATIONS.UNCLE });
            map.put(RELATIONS.SISTER, new RELATIONS[] { RELATIONS.AUNT });
            map.put(RELATIONS.MOTHER, new RELATIONS[] { RELATIONS.GRANDMOTHER });
            map.put(RELATIONS.FATHER, new RELATIONS[] { RELATIONS.GRANDFATHER });
            FAMILYRELATIONS.put(r, map);

            // MOTHER
            r = RELATIONS.MOTHER;
            map = new HashMap<RELATIONS, RELATIONS[]>();
            map.put(RELATIONS.BROTHER, new RELATIONS[] { RELATIONS.UNCLE });
            map.put(RELATIONS.SISTER, new RELATIONS[] { RELATIONS.AUNT });
            map.put(RELATIONS.MOTHER, new RELATIONS[] { RELATIONS.GRANDMOTHER });
            map.put(RELATIONS.FATHER, new RELATIONS[] { RELATIONS.GRANDFATHER });
            FAMILYRELATIONS.put(r, map);

            // UNCLE
            r = RELATIONS.UNCLE;
            map = new HashMap<RELATIONS, RELATIONS[]>();
            map.put(RELATIONS.SON, new RELATIONS[] { RELATIONS.M_COUSIN });
            FAMILYRELATIONS.put(r, map);
        }

        public static RELATIONS[] getFamilyRelation(RELATIONS From, RELATIONS To) {
            if (FAMILYRELATIONS.size() == 0) {
                fillRelations();
            }
            // System.out.println(FAMILYRELATIONS.size());
            // System.out.println(FAMILYRELATIONS.containsKey(new RELATIONS[]
            // {RELATIONS.FATHER, RELATIONS.BROTHER}));
            if (FAMILYRELATIONS.get(From) != null) {
                return FAMILYRELATIONS.get(From).get(To);
            }
            return null;
        }

        RELATIONS(String pronomen, String relation) {
            this.possesivpronomen = pronomen;
            this.relation = relation;
        }

        public static String getPronounString(RELATIONS relationship) {
            // if (relationship == null) {
            // return "";
            // }
            return relationship.getPossesivpronomen();
        }

        public static String getRelationShipString(RELATIONS relationship) {
            // if (relationship == null) {
            // return "";
            // }
            return relationship.getRelation();
        }

        public static RELATIONS getRelationByString(String relation) {
            for (RELATIONS r : RELATIONS.values()) {
                if (r.getRelation().equalsIgnoreCase(relation)) {
                    return r;
                }
            }
            return null;
        }
    }

}