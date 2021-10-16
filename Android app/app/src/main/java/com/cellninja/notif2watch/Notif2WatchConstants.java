package com.cellninja.notif2watch;

/**
 * Created by Roger on 2015-10-22.
 */

public class Notif2WatchConstants {
    public interface ACTION {
        public static String MAIN_ACTION = "com.cellninja.notif2watch.action.main";
        public static String PREV_ACTION = "com.cellninja.notif2watch.action.prev";
        public static String PLAY_ACTION = "com.cellninja.notif2watch.action.play";
        public static String NEXT_ACTION = "com.cellninja.notif2watch.action.next";
        public static String STARTFOREGROUND_ACTION = "com.cellninja.notif2watch.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.cellninja.notif2watch.action.stopforeground";
    }

    public interface EXTRAS {
        public static String TUPLE_VALUES = "TUPLE_VALUES";
        public static String BATTERY_LEVEL = "BATTERY_LEVEL";
        public static String COMMAND_REFRESH_WATCHFACE = "REFRESH_WATCHFACE";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public interface DEFAULT_VALUES {
        public static String LOG_TAG = "Notif2Watch";
        public static String NOTIFICATION_CHANGED = "com.cellninja.notif2watch.NOTIFICATION_CHANGED";
        public static String MESSAGE_PASSING_INTENT_ACTION = "com.cellninja.notif2watch.action.MESSAGE_PASSING_TO_SERVICE";
        public static String BATTERY_CHANGED = "com.cellninja.notif2watch.BATTERY_CHANGED";
        public static String NOTIFICATION_LISTENER_SERVICE = "com.cellninja.notif2watch.NOTIFICATION_LISTENER_SERVICE";
        public static String NOTIFICATION_LISTENER_UI = "com.cellninja.notif2watch.NOTIFICATION_LISTENER_UI";
        public static String VECTOR_NOTIFICATION_INTENT = "com.cellninja.notif2watch.BLACKBERRY_VECTOR_NOTIFICATION";
        public static String VECTOR_PACKAGE_NAME = "com.vectorwatch.android";
        public static String CELLNINJA_VECTOR_APP_NAME = "Bridge";
        public static Integer LISTENING_PORT = 9877;
//        public static Integer LISTENING_PORT = 18745;
//        public static Integer SENDING_PORT = 18746;
        public static Integer H2W_SENDING_PORT = 10771;
        public static Integer BRIDGE_SENDING_PORT = 11771;
        public static Integer MAX_MESSAGE_LENGTH = (4096 * 2);
        public static String N2W_UUID = "28AF3DC7-E40D-490F-BEF2-29548C8BEEA1";
        public static String PREFS_FILE_NAME = "NOTIF2WATCH_PREFS";
    }

    public interface SHARED_PREFS_KEYS {
        public static String FIRST_TIME = "FIRST_TIME";
        public static String N2W_KEY = "N2W_KEY";
    }

    public interface TALK2WATCH_SPECIFIC {
        public static String ITEM_SEPARATOR = "::";
        public static String VALUE_SEPARATOR = "\\$\\$";
        public static String HUB2WATCH_BASE_UUID = "28AF3DC7-E40D-490F-BEF2-29548C8BEEE";
    }

    public interface WATCHFACE_MESSAGE_KEYS {
        public static Integer HUB1_ACCOUNT_KEY = 0;
        public static Integer HUB2_ACCOUNT_KEY = 1;
        public static Integer HUB3_ACCOUNT_KEY = 2;
        public static Integer HUB4_ACCOUNT_KEY = 3;
        public static Integer HUB5_ACCOUNT_KEY = 4;
        public static Integer HUB6_ACCOUNT_KEY = 5;
        public static Integer HUB7_ACCOUNT_KEY = 6;
        public static Integer HUB8_ACCOUNT_KEY = 7;
        public static Integer VIBRATE_KEY = 12;
        public static Integer PHONE_PERCENT = 13;
//  public static Integer CALENDAR_EVENT_NAME = 14;
        public static Integer SLOT_COUNTER_KEY = 0x15;
//  public static Integer SHOW_CALENDAR_SCREEN_KEY = 16;
        public static Integer COLOR_TIME_BACKGROUND = 17;
//  public static Integer TOP_KEY_COLOR_GREEN = 18;
//  public static Integer TOP_KEY_COLOR_BLUE = 19;
        public static Integer COLOR_NOTIFICATION_BACKGROUND = 20;
//  public static Integer BOTTOM_KEY_COLOR_GREEN = 21;
//  public static Integer BOTTOM_KEY_COLOR_BLUE = 22;
        public static Integer COLOR_TIME_TEXT = 23;
//  public static Integer TIME_KEY_COLOR_GREEN = 24;
//  public static Integer TIME_KEY_COLOR_BLUE = 25;
//  public static Integer USER_TOKEN = 26;
        public static Integer NUMBER_OF_SLOTS = 27;
        public static Integer TEMPERATURE_SCALE = 28;
        public static Integer WEATHER_ICON = 50;
        public static Integer WEATHER_TEMPF = 51;
        public static Integer WEATHER_REQUEST = 52;
        public static Integer WEATHER_CITY = 53;
        public static Integer JS_IS_READY = 99;
        public static Integer ASK_NOTIF2WATCH_REFRESH = 100;
    }

    public enum Icons {
        RESOURCE_ID_CLEAR_DAY_34,				// 0
        RESOURCE_ID_CLEAR_NIGHT_34,				// 1
        RESOURCE_ID_CLOUD_ERROR_34,				// 2
        RESOURCE_ID_CLOUDY_34,					// 3
        RESOURCE_ID_COLD_34,					// 4
        RESOURCE_ID_DRIZZLE_34,					// 5
        RESOURCE_ID_FOG_34,						// 6
        RESOURCE_ID_HOT_34,						// 7
        RESOURCE_ID_NOT_AVAILABLE_34,			// 8
        RESOURCE_ID_PARTLY_CLOUDY_DAY_34,		// 9
        RESOURCE_ID_PARTLY_CLOUDY_NIGHT_34,  	// 10
        RESOURCE_ID_PHONE_ERROR_34,				// 11
        RESOURCE_ID_RAIN_34,					// 12
        RESOURCE_ID_RAIN_SLEET_34,				// 13
        RESOURCE_ID_RAIN_SNOW_34,				// 14
        RESOURCE_ID_SLEET_34,					// 15
        RESOURCE_ID_SNOW_34,					// 16
        RESOURCE_ID_SNOW_SLEET_34,				// 17
        RESOURCE_ID_THUNDER_34,					// 18
        RESOURCE_ID_WIND_34,					// 19
        RESOURCE_ID_ICON_BBM_34,  				// 20
        RESOURCE_ID_ICON_CALL_34,				// 21
        RESOURCE_ID_ICON_EMAIL_34,				// 22
        RESOURCE_ID_ICON_FACEBOOK_34,			// 23
        RESOURCE_ID_ICON_LINKEDIN_34,			// 24
        RESOURCE_ID_ICON_NOTIFICATION_34,		// 25
        RESOURCE_ID_ICON_PIN_34,				// 26
        RESOURCE_ID_ICON_TEXT_34,				// 27
        RESOURCE_ID_ICON_TWITTER_34,			// 28
        RESOURCE_ID_ICON_VOICEMAIL_34,			// 29
        RESOURCE_ID_ICON_WHATSAPP_34,			// 30
        RESOURCE_ID_ICON_EMPTY_34,				// 31
        RESOURCE_ID_ICON_BLAQ_34,				// 32
        RESOURCE_ID_ICON_SLACK_34,				// 33
        RESOURCE_ID_ICON_INSTAGRAM_34,			// 34
        RESOURCE_ID_ICON_QUICKPOST_34,			// 35
        RESOURCE_ID_ICON_TWITTLY_34,			// 36
        RESOURCE_ID_ICON_WECHAT_34,				// 37
        RESOURCE_ID_ICON_HG10_34,				// 38
        RESOURCE_ID_ICON_GOOGLE_TALK_34,		// 39
        RESOURCE_ID_ICON_CALENDAR_34,			// 40
        RESOURCE_ID_ICON_VIP_34,				// 41
        RESOURCE_ID_ICON_FOURSQUARE_34,			// 42
        RESOURCE_ID_ICON_FACE10_34,				// 43
        RESOURCE_ID_ICON_JUST10_34,				// 44
        RESOURCE_ID_ICON_VK10_34,				// 45
        RESOURCE_ID_ICON_MESSENGER_34,			// 46
        RESOURCE_ID_ICON_GOOGLE_PLAY_34,		// 47
        RESOURCE_ID_ICON_NOTIF2WATCH_34,		// 48
        RESOURCE_ID_ICON_HUB_34,				// 49
        RESOURCE_ID_ICON_SKYPE_34,				// 50
        RESOURCE_ID_ICON_GMAIL_34,				// 51
        RESOURCE_ID_ICON_SCREENSHOT_34,			// 52
        RESOURCE_ID_ICON_YOUTUBE_34,			// 53
        RESOURCE_ID_ICON_TELEGRAM_34,			// 54
        RESOURCE_ID_ICON_DISCORD_34,			// 55
        RESOURCE_ID_ICON_PUSHBULLET_34,			// 56
        RESOURCE_ID_ICON_LINE_34,				// 57
        RESOURCE_ID_ICON_BADOO_34,				// 58
        RESOURCE_ID_ICON_BOX_34,				// 59
        RESOURCE_ID_ICON_CHROME_DOWNLOAD_34,	// 60
        RESOURCE_ID_ICON_DROPBOX_34,			// 61
        RESOURCE_ID_ICON_FIVERR_34,				// 62
        RESOURCE_ID_ICON_GOOGLE_PLUS_34,		// 63
        RESOURCE_ID_ICON_MEETUP_34,				// 64
        RESOURCE_ID_ICON_ONEDRIVE_34,			// 65
        RESOURCE_ID_ICON_PAYPAL_34,				// 66
        RESOURCE_ID_ICON_PERISCOPE_34,			// 67
        RESOURCE_ID_ICON_PINTEREST_34,			// 68
        RESOURCE_ID_ICON_SNAPCHAT_34,			// 69
        RESOURCE_ID_ICON_TINDER_34,				// 70
        RESOURCE_ID_ICON_TUMBLR_34,				// 71
        RESOURCE_ID_ICON_VIBER_34,				// 72
        RESOURCE_ID_ICON_VINE_34,				// 73
        RESOURCE_ID_ICON_GRINDR_34,				// 74
        RESOURCE_ID_ICON_GOOGLE_PHOTOS_34,		// 75
        RESOURCE_ID_ICON_DELIVERIES_34,	        // 76
        RESOURCE_ID_ICON_TETHERING_34,			// 77
        RESOURCE_ID_ICON_GOOGLE_DRIVE_34,   	// 78
        RESOURCE_ID_ICON_ANDROID_34,         	// 79
        RESOURCE_ID_ICON_GOOGLE_KEEP_34,		// 80
        RESOURCE_ID_ICON_EUROSPORT_34,         	// 81
        RESOURCE_ID_ICON_LE_MONDE_34,         	// 82
        RESOURCE_ID_ICON_THREEMA_34,    		// 83
        RESOURCE_ID_ICON_SZDE_34,    	    	// 84
        RESOURCE_ID_ICON_DB_NAVIGATOR_34,    	// 85
        RESOURCE_ID_ICON_RUNNER_34,		    	// 86
        RESOURCE_ID_ICON_MUSIC_34,		    	// 87
        RESOURCE_ID_ICON_GOOGLE_ALLO_34,	   	// 88
        RESOURCE_ID_ICON_DASHBOARD_34,  	   	// 89
        RESOURCE_ID_ICON_SIGNAL_34, 	 	   	// 90
   };

    public static String[] APP_NAMES = {
        "RESOURCE_ID_CLEAR_DAY_34",             // 0
        "RESOURCE_ID_CLEAR_NIGHT_34",           // 1
        "RESOURCE_ID_CLOUD_ERROR_34",           // 2
        "RESOURCE_ID_CLOUDY_34",                // 3
        "RESOURCE_ID_COLD_34",					// 4
        "RESOURCE_ID_DRIZZLE_34",				// 5
        "RESOURCE_ID_FOG_34",					// 6
        "RESOURCE_ID_HOT_34",					// 7
        "RESOURCE_ID_NOT_AVAILABLE_34",			// 8
        "RESOURCE_ID_PARTLY_CLOUDY_DAY_34",		// 9
        "RESOURCE_ID_PARTLY_CLOUDY_NIGHT_34",  	// 10
        "RESOURCE_ID_PHONE_ERROR_34",			// 11
        "RESOURCE_ID_RAIN_34",					// 12
        "RESOURCE_ID_RAIN_SLEET_34",			// 13
        "RESOURCE_ID_RAIN_SNOW_34",				// 14
        "RESOURCE_ID_SLEET_34",					// 15
        "RESOURCE_ID_SNOW_34",					// 16
        "RESOURCE_ID_SNOW_SLEET_34",			// 17
        "RESOURCE_ID_THUNDER_34",				// 18
        "RESOURCE_ID_WIND_34",					// 19
        "BBM",  				                // 20
        "Call",				                    // 21
        "Mail",				                    // 22
        "Facebook",			                    // 23
        "LinkedIn",			                    // 24
        "Notification",		                    // 25
        "Pin",				                    // 26
        "SMS",				                    // 27
        "Twitter",			                    // 28
        "Voicemail",			                // 29
        "WhatsApp",			                    // 30
        "RESOURCE_ID_ICON_EMPTY_34",			// 31
        "Blaq",				                    // 32
        "Slack",				                // 33
        "Instagram",			                // 34
        "QuickPost",			                // 35
        "Twittly",			                    // 36
        "WeChat",				                // 37
        "Hg10",				                    // 38
        "Talk",		                            // 39 (Google Talk)
        "Calendar",			                    // 40
        "VIP",				                    // 41
        "Foursquare",			                // 42
        "Face10",				                // 43
        "Just10",				                // 44
        "VK",				                    // 45
        "Messenger",			                // 46
        "Play",		                            // 47 (Google Play)
        "N2W",		                            // 48
        "Hub",				                    // 49
        "Skype",				                // 50
        "GMail",				                // 51
        "Screenshot",			                // 52
        "Youtube",			                    // 53
        "Telegram",			                    // 54
        "Discord",			                    // 55
        "PushBullet",			                // 56
        "LINE",				                    // 57
        "Badoo",				                // 58
        "Box",				                    // 59
        "Download",	                            // 60
        "DropBox",			                    // 61
        "Fiverr",				                // 62
        "Google+",		                            // 63
        "Meetup",				                // 64
        "OneDrive",			                    // 65
        "PayPal",				                // 66
        "Periscope",			                // 67
        "Pinterest",			                // 68
        "Snapchat",			                    // 69
        "Tinder",				                // 70
        "Tumblr",				                // 71
        "Viber",				                // 72
        "Vine",				                    // 73
        "Grindr",				                // 74
        "Photos",		                        // 75 (Google Photos)
        "Deliveries",	                        // 76
        "Tether",			                    // 77
        "Drive",   	                            // 78 (Google Drive)
        "Android",         	                    // 79
        "Keep",		                            // 80
        "EuroSport",         	                // 81
        "LeMonde",         	                    // 82
        "Threema",    		                    // 83
        "SZDE",    	    	                    // 84
        "DbNavigator",    	                    // 85
        "Runner",		    	                // 86
        "Music",		    	                // 87
        "Allo",	   	                            // 88
        "Dashboard",  	   	                    // 89
        "Signal" 	 	   	                    // 90
    };
}