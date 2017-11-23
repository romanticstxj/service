package com.madhouse.platform.premiummad.util;



public final class Constant {

    public static final class PlcmtType {
        public static final int BANNER = 1;
        public static final int VIDEO = 2;
        public static final int NATIVE = 3;
    }

    public static final class NativeImageType {
        public static final int MAIN = 1;
        public static final int ICON = 2;
        public static final int COVER = 3;
    }
    public static final class Layout{
        public static final int BANNER_101 = 101; //Banner-横幅 
        public static final int BANNER_102 = 102; //Banner-焦点 
        public static final int BANNER_111 = 111; //Banner-插屏 
        public static final int BANNER_112 = 112; //Banner-全屏 
        public static final int BANNER_121 = 121; //Banner-开屏 
        public static final int BANNER_131 = 131; //Banner-开机图片
        public static final int BANNER_132 = 132; //Banner-关机图片
        public static final int VIDEO_201 = 201; //Video-贴片/前贴
        public static final int VIDEO_202 = 202; //Video-贴片/中贴
        public static final int VIDEO_203 = 203; //Video-贴片/后贴
        public static final int VIDEO_211 = 211; //Video-悬浮/暂停
        public static final int VIDEO_221 = 221; //Video-开机视频
        public static final int VIDEO_222 = 222; //Video-关机视频
        public static final int VIDEO_231 = 231; //Video-开屏视频
        public static final int NATIVE_301 = 301; //Native-图文信息流 
        public static final int NATIVE_302 = 302; //Native-图文信息流
        public static final int NATIVE_303 = 303; //Native-图文信息流
        public static final int NATIVE_311 = 311; //Native-视频信息流
    }
    public static final class Linearity{
        /**
         * 贴片视频
         */
        public static final int PATCH_VIDEO = 1;
        /**
         * 悬浮/暂停
         */
        public static final int SUSPENSION_PAUSE = 2;
        /**
         * 开机视频
         */
        public static final int BOOT_VIDEO = 3;
        /**
         * 关机视频
         */
        public static final int SHUTDOWN_VIDEO = 4;
        /**
         * 开屏视频
         */
        public static final int SCREEN_VIDEO = 5;
        
    }
    //0：前贴、-1：中贴、-2：后贴；>0：实际秒数；
    public static final class StartDelay{
        public static final int BEFORE_COMMENCEMENT = 0;
        public static final int IN_THE_POST = -1;
        public static final int POST = -2;
        
    }

    
    public static final class NativeDescType {
        public static final int DESC = 1;
    }
    
    public static final class DeliveryType {
        public static final int PDB = 1;
        public static final int PD = 2;
        public static final int PMP = 4;
        public static final int RTB = 8;
    }
    public static final class BlockType {
        public static final int DID = 1;
        public static final int DPID = 2;
        public static final int IFA = 3;
        public static final int IP = 4;
    }
}
