param test1 := 15.4;
param test2 := 2 - 5 integer;
param test3 := test1 + test2;
param test4 := 2^4 - 8;
param test5 := 2^4/2 - 8/2;
param test6 := 2^4/(2 - 8);
param test7 := 2^2^2;
param test8 := 2^4/(2 - 8)/3;
param test9 := 8/4^2/2;
param test10 := -test1;
var v1 integer >= 0 <= 1000;
var v2 integer := v1^(5+1);
var v3 := v2;
var v4 := v1+v3^(v2+test9^v1);
var v5 := 5*v1 + v2/4;
var v6 binary;
var v7;
var v8 := -v2;
var v9 := +v2;

maximize objectiveName1: 5*v1 + (1/2)*v2;
minimize objectiveName2: 5*v1 + (1/2)*v2;

subject to constr1 : v1 <= 4;
constr2 : v2 = 5;