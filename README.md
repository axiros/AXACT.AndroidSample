# AXACT

`Axiros AXACT - Android Wrapper` is a wrapper for `AXACT`

This project contains a sample application to demostrate how to use TR-069 and TR-143.
`AXACT` is embedded in this project as an android library.

<p align="center" >
<img src="http://bzero.github.io/assets/images/Screenshot_2017-07-24-10-59-14.png" width="800" height="500"/>
</p>

This version opens a blank activity `AXACT` is started as service on application run and is configured to run as background service. in order to stop the service uses developer options to see running tasks:

<p align="center" >
<img src="http://bzero.github.io/assets/images/Screenshot_2017-07-12-10-04-26.png" width="800" height="500"/>
</p>

or call, fron any Activity:

```
stopService(new Intent(this, AxirosService.class));
```


## LIB proguard rules

To use minifyEnabled build on your APP please add the following line to your proguard-rules.pro:

```
-keep public interface com.axiros.axact.AXACTEvents {*;}
```

## Compiling on Android 6.0 (API level 23)

Beginning in Android 6.0 (API level 23), users grant permissions to apps while it
is running not when they install it. In order to request the required SDK permissions,
please be sure to call the _verifyServicePermission_ method before binding the
service and implement the _onRequestPermissionsResult_. Its return value will tell
where the bind can be made. A full sample can be found on MainActivity.

```
@Override
public void onRequestPermissionsResult(int requestCode, String permissions[],
                                       int[] grantResults)
{
    /*
     * The option has already been chosen by the user. The bind can happen
     * here.
     */
}

@Override
public void onCreate(Bundle savedInstanceState) {
    if (AxirosService..verifyServicePermission(MainActivity.this) == false) {
        /*
         * Permissions were already given by the user. The bind can happen
         * here.
         */
    }
}
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
