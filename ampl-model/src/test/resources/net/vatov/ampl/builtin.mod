var x1 := abs(-1);
var x2 := acos(1);
var x3 := acosh(1);
var x4 := asin(1);
var x5 := asinh(1);
var x6 := atan(1);
var x7 := atan2(1,2);
var x8 := atanh(1);
var x9 := ceil(1.1);
var x10 := ctime();
var x11 := ctime(time());
var x12 := cos(1);
var x13 := exp(1);
var x14 := floor(1.1);
var x15 := log(1);
var x16 := log10(1);
var x17 := max(1+1, 1+2, 6, 6^2, 0);
var x18 := min(1+1, 1+2, 6, 6^2, 0);
var x19 := precision(1.12345, 2);
var x20 := round(1.12345);
var x21 := round(1.12345, 2);
var x22 := sin(1);
var x23 := sinh(1);
var x24 := sqrt(4);
var x25 := tan(1);
var x26 := tanh(1);
var x27 := time();
var x28 := trunc(1.123456);
var x29 := trunc(1.123456, 2);

maximize objectiveName1: 5*x1 + (1/2)*x2;
minimize objectiveName2: 5*x1 + (1/2)*x2;
