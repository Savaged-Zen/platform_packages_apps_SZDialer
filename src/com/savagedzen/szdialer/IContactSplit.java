package com.savagedzen.szdialer;

import android.database.Cursor;
import android.net.Uri;

interface IContactSplit {
	public Uri getLookupUri(Cursor c);
	public String getDisplayName(Cursor c);

	/**
	 * Determine a Uri for sending to Intent.ACTION_CALL or NULL if number can't be determined.
	 * @param lookupUri
	 * @return
	 */
	public Uri getCallUri(Uri lookupUri);

	/**
	 * Determine a Uri for sending to Intent.ACTION_VIEW. Never returns NULL. 
	 * @param lookupUri
	 * @return
	 */
	public Uri getContactUri(Uri lookupUri);
}
