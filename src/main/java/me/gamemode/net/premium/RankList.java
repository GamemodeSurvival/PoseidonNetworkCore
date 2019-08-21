package me.gamemode.net.premium;

public class RankList {

	public static enum ranks {
		NONE(11), MEMBER(10), VIP(9), VIPPLUS(8), MVP(7), MVPPLUS(6), ULTRA(8), BUILDER(5), DEVELOPER(4), TRAINEE(3),
		MOD(2), MODPLUS(1), ADMIN(0);

		private final int priority;

		ranks(int priority) {
			this.priority = priority;
		}

		public int getPriority() {
			return this.priority;
		}
	}

}
