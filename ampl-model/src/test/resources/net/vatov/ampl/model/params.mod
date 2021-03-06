var x1;
var x2 integer;
var x3 := 15 + x1;
var x4 := 4;
var x5 integer >=0 <=100 := 15;
var x6 binary;
var x7 := -1;
var x8 := -x7;
var x9 := +1;
var x10 := +x7;

param p1 := -1;
param p2 := -p1;
param p3 := +p1;
param p4 := +1;

var v1 := abs(0-1);
var v2 := acos(1);
var v3 := acosh(1);
var v4 := asin(1);
var v5 := asinh(1);
var v6 := atan(1);
var v7 := atan2(1,2);
var v8 := atanh(1);
var v9 := ceil(1.1);
var v10 := ctime();
var v11 := ctime(time());
var v12 := cos(1);
var v13 := exp(1);
var v14 := floor(1.1);
var v15 := log(1);
var v16 := log10(1);
var v17 := max(1+1, 1+2, 6, 6^2, 0);
var v18 := min(1+1, 1+2, 6, 6^2, 0);
var v19 := precision(1.12345, 2);
var v20 := round(1.12345);
var v21 := round(1.12345, 2);
var v22 := sin(1);
var v23 := sinh(1);
var v24 := sqrt(4);
var v25 := tan(1);
var v26 := tanh(1);
var v27 := time();
var v28 := trunc(1.123456);
var v29 := trunc(1.123456, 2);

maximize goal: 2*x1 + 3*x2 - 2*x3 + 3*x4;

c1: 3*x1 + 2*x2 + 2*x3 + x4 <= 4;
c2:        4*x2 + 3*x3 + x4 >= 3;