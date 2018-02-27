package pt.ulusofona.copelabs.oi_ndn.helpers;

/**
 * This class contains all the information about the prefixes that are used in Oi! application.
 *
 * @author Omar Aponte (COPELABS/ULHT)
 * @version 1.0
 *          COPYRIGHTS COPELABS/ULHT, LGPLv3.0, 02/14/18
 */
public abstract class NameModule {

    /**
     * Firefighter identifier.
     */
    public static final int FIREFIGHTER_ID = 1;
    /**
     * Police identifier.
     */
    public static final int POLICE_ID = 2;
    /**
     * Special service identifier.
     */
    public static final int SPECIAL_SERVICE_ID = 3;
    /**
     * People nearby identifier.
     */
    public static final int PEOPLE_NEARBY_ID = 4;
    /**
     * Rescue team identifier.
     */
    public static final int RESCUE_TEAM_ID = 5;

    /**
     * Application name used to integrate prefixes.
     */
    public static final String APPLICATION_NAME = "oi";

    /**
     * General Prefix name.
     */
    public static final String GENERAL_PREFIX = "/ndn/multicast/";

    /**
     * Base prefix used as initial part. To this prefix could be added more information in order
     * to have more granularity in the name schema.
     */
    public static final String PREFIX = GENERAL_PREFIX + APPLICATION_NAME;

    /**
     * Emergency prefix.
     */
    public static final String EMERGENCY_PREFIX = PREFIX + "/emergency/";

    /**
     * Police name.
     */
    public static final String POLICE_NAME = "police";
    /**
     * Firefighter name.
     */
    public static final String FIREFIGHTER_NAME = "firefighter";
    /**
     * Special service name.
     */
    public static final String SPECIAL_SERVICE_NAME = "specialservice";
    /**
     * People nearby name.
     */
    public static final String PEOPLE_NEARBY_NAME = "peoplenearby";
    /**
     * Rescue team name.
     */
    public static final String RESCUE_TEAM_NAME = "rescueteam";

    /**
     * This function creates a prefix base on the emergency option received.
     *
     * @param option Integer with the option. those option could be the identifier of the authority.
     * @return String with the prefix to be used in emergency cases.
     */
    public static String getEmergencyPrefix(int option) {
        String prefix = null;
        switch (option) {
            case FIREFIGHTER_ID:
                prefix = EMERGENCY_PREFIX + FIREFIGHTER_NAME;
                break;
            case POLICE_ID:
                prefix = EMERGENCY_PREFIX + POLICE_NAME;
                break;
            case SPECIAL_SERVICE_ID:
                prefix = EMERGENCY_PREFIX + SPECIAL_SERVICE_NAME;
                break;
            case PEOPLE_NEARBY_ID:
                prefix = EMERGENCY_PREFIX + PEOPLE_NEARBY_NAME;
                break;
            case RESCUE_TEAM_ID:
                prefix = EMERGENCY_PREFIX + RESCUE_TEAM_NAME;
                break;
        }
        return prefix;
    }

}
