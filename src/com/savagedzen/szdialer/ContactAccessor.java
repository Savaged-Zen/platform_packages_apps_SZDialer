/*
 *
 * Copyright (C) 2010 Savaged-Zen
 * SZDialer is free software. It is based upon NubDial and as such, is under
 * the terms * SpellDial is free software: you can redistribute it and/or modify
 * of the GNU General Public License as published by the Free Software Foundation,
 * either * it under the terms of the GNU General Public  License as published by
 * version 2 of the License, or (at your option) any later version. Tthe Free
 * Software Foundation, either version 2 of the License, or  (at your option) any later version.
 *
 * Copyright (C) 2010 Savaged-Zen * Copyright (C) 2010 Lawrence Greenfield
 *  SpellDial is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with SpellDial.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
/* Derived from
 * http://code.google.com/p/android-business-card/source/browse/trunk/android-business-card/BusinessCard/src/com/example/android/businesscard/ContactAccessor.java
 *
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.savagedzen.szdialer;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;

abstract class ContactAccessor {
	public abstract Cursor recalculate(String filter, boolean matchNumAnywhere);
	public abstract IContactSplit getContactSplit();
	public abstract Intent getContactsIntent();
	public abstract Intent getFavouritesIntent();
	public abstract Intent addToContacts(String number);	
	public abstract void setContentResolver(ContentResolver cr);
	
	public Intent getCallLogIntent() {
	    Intent  intent = new Intent(Intent.ACTION_VIEW, null);
        intent.setType("vnd.android.cursor.dir/calls");
        return intent;
	}
	
    /**
     * Static singleton instance of {@link ContactAccessor} holding the
     * SDK-specific implementation of the class.
     */
    private static ContactAccessor sInstance;

    public static synchronized ContactAccessor getInstance(ContentResolver cr) {
        if (sInstance == null) {
            String className;

            /*
             * Check the version of the SDK we are running on. Choose an
             * implementation class designed for that version of the SDK.
             *
             * Unfortunately we have to use strings to represent the class
             * names. If we used the conventional ContactAccessorSdk5.class.getName()
             * syntax, we would get a ClassNotFoundException at runtime on pre-Eclair SDKs.
             * Using the above syntax would force Dalvik to load the class and try to
             * resolve references to all other classes it uses. Since the pre-Eclair
             * does not have those classes, the loading of ContactAccessorSdk5 would fail.
             */
            int sdkVersion = Integer.parseInt(Build.VERSION.SDK);       // Cupcake style
            if (sdkVersion < Build.VERSION_CODES.ECLAIR) {
                className = "com.savagedzen.szdialer.ContactAccessorSdk3_4";
            } else {
                className = "com.savagedzen.szdialer.ContactAccessorSdk5";
            }

            /*
             * Find the required class by name and instantiate it.
             */
            try {
                Class<? extends ContactAccessor> clazz =
                        Class.forName(className).asSubclass(ContactAccessor.class);
                sInstance = clazz.newInstance();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
            sInstance.setContentResolver(cr);
        }

        return sInstance;
    }
}
