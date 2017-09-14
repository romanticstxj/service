package com.madhouse.platform.premiummad.constant;

public final class AdspaceConstant {

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

	public static final class Linearity {
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

	// 0：前贴、-1：中贴、-2：后贴；>0：实际秒数；
	public static final class StartDelay {
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
}
