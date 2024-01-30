package org.op;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;

public class OpPlugin extends Plugin {
	
	@Override
	public void onLoad() {
		
		//logger
		this.getLogger().info("beginning initialization!");

		final AutoPearl autopearl = new AutoPearl();
		RusherHackAPI.getModuleManager().registerFeature(autopearl);

		final TrapEsp trapesp = new TrapEsp();
		RusherHackAPI.getModuleManager().registerFeature(trapesp);

		this.getLogger().info("all modules initialized!");
	}
	
	@Override
	public void onUnload() {
		this.getLogger().info("unloaded");
	}
	
	@Override
	public String getName() {
		return "op";
	}
	
	@Override
	public String getVersion() {
		return "v1.0.1";
	}
	
	@Override
	public String getDescription() {
		return "buncha stuff";
	}
	
	@Override
	public String[] getAuthors() {
		return new String[]{"op"};
	}
}
