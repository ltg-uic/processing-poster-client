/**
 * *******************************************************************
 * TERMS OF USE - EASING EQUATIONS
 * Open source under the BSD License.
 * Copyright (c) 2001 Robert Penner
 * JavaScript version copyright (C) 2006 by Philippe Maegerman
 * All rights reserved.
 * <p/>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * <p/>
 * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 * Neither the name of the author nor the names of contributors may
 * be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <p/>
 * ***************************************
 */

package ltg.evl.util.de.looksgood.ani.easing;

public class Expo extends Easing {
    public Expo(int theEasingMode) {
        setMode(theEasingMode);
    }

    public Expo() {
        setMode(easingMode);
    }

    public float easeIn(float t, float b, float c, float d) {
        return (t == 0) ? b : c * (float) Math.pow(2, 10 * (t / d - 1)) + b;
    }

    public float easeOut(float t, float b, float c, float d) {
        return (t == d) ? b + c : c * (-(float) Math.pow(2, -10 * t / d) + 1) + b;
    }

    public float easeInOut(float t, float b, float c, float d) {
        if (t == 0)
            return b;
        if (t == d)
            return b + c;
        if ((t /= d / 2) < 1)
            return c / 2 * (float) Math.pow(2, 10 * (t - 1)) + b;
        return c / 2 * (-(float) Math.pow(2, -10 * --t) + 2) + b;
    }
}
