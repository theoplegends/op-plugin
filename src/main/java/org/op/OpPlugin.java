package org.op;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;

/**
 * Example rusherhack plugin USED AS BASE
 *
 * @author John200410
 */
public class OpPlugin extends Plugin {
	
	@Override
	public void onLoad() {
		
		//logger
		this.getLogger().info("beginning initialization!");

		final AutoPearl autopearl = new AutoPearl();
		RusherHackAPI.getModuleManager().registerFeature(autopearl);
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