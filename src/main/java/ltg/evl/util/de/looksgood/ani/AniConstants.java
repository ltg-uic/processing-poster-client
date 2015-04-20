/*
Ani (a processing animation library) 
Copyright (c) 2010 Benedikt Gro�

http://www.looksgood.de/libraries/Ani

Standing on the shoulders of giants:
Jack Doyle - TweenLite AS3 Library (http://blog.greensock.com/tweenlite/)
Robert Penner - Equations (http://robertpenner.com/easing/)
Andreas Schlegel - ControlP5 (http://www.sojamo.de/libraries/);
Ekene Ijeoma - Tween Processin Library (http://www.ekeneijeoma.com/processing/tween/)

AniCore, Ani and AniSequence includes many ideas and code of the nice people above!
Thanks a lot for sharing your code with the rest of the world!

This library is free software; you can redistribute it and/or modify it under the terms 
of the GNU Lesser General Public License as published by the Free Software Foundation; 
either version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with this 
library; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, 
Boston, MA 02110, USA
*/

package ltg.evl.util.de.looksgood.ani;

import ltg.evl.util.de.looksgood.ani.easing.*;

/**
 * All Constants of the Ani library.
 */
public interface AniConstants {

    String VERSION = "2.6";

    // timeMode
    String SECONDS = "SECONDS";
    String FRAMES = "FRAMES";

    // callback, keywords for the property list string parsing
    String ON_START = "onStart";
    String ON_END = "onEnd";
    String ON_DELAY_END = "onDelayEnd";
    String ON_UPDATE = "onUpdate";

    // playMode
    String FORWARD = "FORWARD";
    String BACKWARD = "BACKWARD";
    String YOYO = "YOYO";

    // autoStartMode
    String AUTOSTART = "AUTOSTART";
    String NO_AUTOSTART = "NO_AUTOSTART";

    // overwriteMode
    String OVERWRITE = "OVERWRITE";
    String NO_OVERWRITE = "NO_OVERWRITE";

    // debug out
    String ANI_DEBUG_PREFIX = "### Ani Debug -> ";

    // easings mode
    int IN = 0;
    int IN_OUT = 2;
    int OUT = 1;

    // names of easings
    Easing LINEAR = new Linear();
    Easing QUAD_IN = new Quad(IN);
    Easing QUAD_OUT = new Quad(OUT);
    Easing QUAD_IN_OUT = new Quad(IN_OUT);
    Easing CUBIC_IN = new Cubic(IN);
    Easing CUBIC_OUT = new Cubic(OUT);
    Easing CUBIC_IN_OUT = new Cubic(IN_OUT);
    Easing QUART_IN = new Quart(IN);
    Easing QUART_OUT = new Quart(OUT);
    Easing QUART_IN_OUT = new Quart(IN_OUT);
    Easing QUINT_IN = new Quint(IN);
    Easing QUINT_OUT = new Quint(OUT);
    Easing QUINT_IN_OUT = new Quint(IN_OUT);
    Easing SINE_IN = new Sine(IN);
    Easing SINE_OUT = new Sine(OUT);
    Easing SINE_IN_OUT = new Sine(IN_OUT);
    Easing CIRC_IN = new Circ(IN);
    Easing CIRC_OUT = new Circ(OUT);
    Easing CIRC_IN_OUT = new Circ(IN_OUT);
    Easing EXPO_IN = new Expo(IN);
    Easing EXPO_OUT = new Expo(OUT);
    Easing EXPO_IN_OUT = new Expo(IN_OUT);
    Easing BACK_IN = new Back(IN);
    Easing BACK_OUT = new Back(OUT);
    Easing BACK_IN_OUT = new Back(IN_OUT);
    Easing BOUNCE_IN = new Bounce(IN);
    Easing BOUNCE_OUT = new Bounce(OUT);
    Easing BOUNCE_IN_OUT = new Bounce(IN_OUT);
    Easing ELASTIC_IN = new Elastic(IN);
    Easing ELASTIC_OUT = new Elastic(OUT);
    Easing ELASTIC_IN_OUT = new Elastic(IN_OUT);

}

