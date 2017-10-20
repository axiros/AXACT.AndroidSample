# AXACT

`Axiros AXACT - Android Wrapper` is a wrapper for `AXACT`


This project contains a sample application to demostrate how to use TR-069 and TR-143.
`AXACT` is embedded in this project as an android library.

<p align="center" >
<img src="http://bzero.github.io/assets/images/Screenshot_2017-07-24-10-59-14.png" width="800" height="500"/>
</p>

This version opens a blank activity `AXACT` is started as service on application run. 

<p align="center" >
<img src="http://bzero.github.io/assets/images/Screenshot_2017-07-12-10-04-26.png" width="800" height="500"/>
</p>


## LIB proguard rules

To use minifyEnabled build on your APP please add the following line to your proguard-rules.pro:

```
-keep public interface com.axiros.axact.AXACTEvents {*;}
```

## Compiling on Android 6.0 (API level 23)

Beginning in Android 6.0 (API level 23), users grant permissions to apps while the app is running, not when they install the app.
In order to request server permissions, please be sure your bind the service in your APP and call verifyServicePermission method, a full sample can be found on MainActivity.

```
            mService.verifyServicePermission(mActivity);
```

## Tested intensively on multiple devices at AWS Device Farm

As it is compiled using `Android NDK` it can run on multiples android version.

<p align="center" >
  <img src="http://bzero.github.io/assets/images/v13/ASUSNexus7-2ndGen-6.0.jpg" width="266" height="500"/>
  <img src="http://bzero.github.io/assets/images/v13/LGNexus4-4.4.3.jpg" width="266" height="500"/>
  <img src="http://bzero.github.io/assets/images/v13/MotorolaMotoG-4.4.4.jpg" width="266" height="500"/>
  <img src="http://bzero.github.io/assets/images/v13/MotorolaMotoG4-7.0.jpg" width="266" height="500"/>
  <img src="http://bzero.github.io/assets/images/v13/MotorolaMotoX-5.1.jpg" width="266" height="500"/>
  <img src="http://bzero.github.io/assets/images/v13/SamsungGalaxyJ1Ace-4.4.4.jpg" width="266" height="500"/>
  <img src="http://bzero.github.io/assets/images/v13/SamsungGalaxyJ1Duos-4.4.4.jpg" width="266" height="500"/>
  <img src="http://bzero.github.io/assets/images/v13/SamsungGalaxyS4-4.4.2.jpg" width="266" height="500"/>
  <img src="http://bzero.github.io/assets/images/v13/SamsungGalaxyS5-4.4.4.jpg" width="266" height="500"/>
</p>

<p align="center" >
  <img src="http://bzero.github.io/assets/images/Screenshot_20170712-101615.png" width="266" height="500"/>
  <img src="http://bzero.github.io/assets/images/Screenshot_20170712-101618.png" width="266" height="500"/>
</p>
