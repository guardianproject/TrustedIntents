
This is a library for flexible trusted interactions between Android apps.  It
is modeled after Android's `signature` protection level for permissions.  The
key difference is that the framework allows the trusted signature to be set,
rather than requiring to match the current app's signature.

For more info:

* https://dev.guardianproject.info/projects/bazaar/wiki/Trusted_Intent_Interaction
* https://guardianproject.info/2014/01/21/improving-trust-and-flexibility-in-interactions-between-android-apps/
* https://developer.android.com/guide/topics/manifest/permission-element.html#plevel

License
-------

This library is licensed under the LGPLv2.1.  We believe this is compatible
with all reasonable uses, including proprietary software, but let us know if
it provides difficulties for you.  For more info on how that works with Java,
see:

https://www.gnu.org/licenses/lgpl-java.en.html
