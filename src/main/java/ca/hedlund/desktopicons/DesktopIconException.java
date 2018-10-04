/*
 * Copyright (C) 2012-2018 Gregory Hedlund
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *    http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.hedlund.desktopicons;

public class DesktopIconException extends Exception {

	private static final long serialVersionUID = -8611604112173673565L;

	public DesktopIconException() {
		super();
	}

	public DesktopIconException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DesktopIconException(String message, Throwable cause) {
		super(message, cause);
	}

	public DesktopIconException(String message) {
		super(message);
	}

	public DesktopIconException(Throwable cause) {
		super(cause);
	}
	
}
