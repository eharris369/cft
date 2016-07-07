/*******************************************************************************
 * Copyright (c) 2012, 2016 Pivotal Software, Inc. and others 
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution. 
 * 
 * The Eclipse Public License is available at 
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * and the Apache License v2.0 is available at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * You may elect to redistribute this code under either of these licenses.
 *  
 *  Contributors:
 *     Pivotal Software, Inc. - initial API and implementation
 ********************************************************************************/
package org.eclipse.cft.server.core.internal;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.security.storage.EncodingUtils;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;

/**
 * @author Steffen Pingel
 */
public class ServerCredentialsStore {

	private static final String KEY_PASSWORD = "password"; //$NON-NLS-1$

	private static final String KEY_USERNAME = "username"; //$NON-NLS-1$
	
	private static final String KEY_TOKEN = "token"; //$NON-NLS-1$

	private boolean initialized;

	private String password;

	private String token;

	private AtomicBoolean securePreferencesDisabled = new AtomicBoolean(false);

	private String serverId;

	private String username;

	private final String nodeId; 
	
	public ServerCredentialsStore(String serverId) {
		this(serverId, CloudFoundryPlugin.PLUGIN_ID);
	}

	protected ServerCredentialsStore(String serverId, String nodeId) {
		this.serverId = serverId;
		this.nodeId = nodeId;
	}
	
	public boolean flush(String newServerId) {
		String oldServerId = getServerId();

		if (oldServerId != null && !oldServerId.equals(newServerId)) {
			initialize();
			ISecurePreferences preferences = getSecurePreferences();
			if (preferences != null) {
				preferences.removeNode();
			}
		}
		
		this.serverId = newServerId;
		
		ISecurePreferences preferences = getSecurePreferences();
		if (preferences != null) {
			try {
				preferences.put(KEY_USERNAME, username, true);
				preferences.put(KEY_PASSWORD, password, true);
				preferences.put(KEY_TOKEN, token, true);
				return true;
			}
			catch (StorageException e) {
				disableSecurePreferences(e);
			}
		}
		return false;
	}

	public String getPassword() {
		initialize();
		return password;
	}

	public String getServerId() {
		return serverId;
	}
	
	public String getUsername() {
		initialize();
		return username;
	}

	public void setPassword(String password) {
		initialize();
		this.password = password;
	}

	public void setUsername(String username) {
		initialize();
		this.username = username;
	}
	
	public String getToken() {
		initialize();
		return token;
	}

	public void setToken(String token) {
		initialize();
		this.token = token;
	}

	private void disableSecurePreferences(StorageException e) {
		if (!securePreferencesDisabled.getAndSet(true)) {
			CloudFoundryPlugin
					.getDefault()
					.getLog()
					.log(new Status(IStatus.ERROR, CloudFoundryPlugin.PLUGIN_ID, "Unexpected error while accessing secure preferences for server: " + serverId)); //$NON-NLS-1$
		}
	}

	private ISecurePreferences getSecurePreferences() {
		if (!securePreferencesDisabled.get()) {
			String serverId = getServerId();
			if (serverId != null) {
				ISecurePreferences securePreferences = SecurePreferencesFactory.getDefault().node(nodeId);
				securePreferences = securePreferences.node(EncodingUtils.encodeSlashes(serverId));
				return securePreferences;
			}
		}
		return null;
	}
	
	private synchronized void initialize() {
		if (initialized) {
			return;
		}
		
		initialized = true;
		username = readProperty(KEY_USERNAME);
		password = readProperty(KEY_PASSWORD);
		token = readProperty(KEY_TOKEN);
	}
	
	private String readProperty(String property) {
		ISecurePreferences preferences = getSecurePreferences();
		if (preferences != null) {
			try {
				return preferences.get(property, null);
			}
			catch (StorageException e) {
				disableSecurePreferences(e);
			}
		}
		return null;
	}
	
}
