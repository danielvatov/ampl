/*
neprekasnati i neogranocheni
*/
var x1;
var x3;
var x6;
var x8;

var x2 >=0;
var x4 >=20 <=80 integer;
var x5 >=0;
var x7 >=0;
var x9 >=60 <=100;
var x10 >=0 integer;

param p1 := 10;

maximize o1: x1 + x2 + x3 +    x4 +    x5 +    x6 +    x7 +    x8 +    x9 + p1*x10;
maximize o2: x1 + x2 + x3 +    x4 +    x5 +    x6 +    x7 +    x8 + p1*x9 +    x10;
minimize o3: x1 + x2 + x3 +    x4 +    x5 +    x6 +    x7 + p1*x8 +    x9 +    x10;
minimize o4: x1 + x2 + x3 +    x4 +    x5 +    x6 + p1*x7 +    x8 +    x9 +    x10;
maximize o5: x1 + x2 + x3 +    x4 +    x5 + p1*x6 +    x7 +    x8 +    x9 +    x10;
maximize o6: x1 + x2 + x3 +    x4 + p1*x5 +    x6 +    x7 +    x8 +    x9 +    x10;
minimize o7: x1 + x2 + x3 + p1*x4 +    x5 +    x6 +    x7 +    x8 +    x9 +    x10;

subject to

c1:  1*x1 + 2*x2 + 3*x3 +  4*x4 +  5*x5 +  6*x6 +  7*x7 + 8*x8 + 9*x9 + 10*x10 <= 100;
c2:  1*x1 + 2*x2 + 3*x3 +  4*x4 +  4*x5 +  4*x6 + 60*x7 + 4*x8 + 4*x9 +  4*x10 <= 10;
c3:  1*x1 + 2*x2 + 3*x3 +  4*x4 + 50*x5 +    x6 +    x7 +   x8 +   x9 +    x10 <= 10000;
c4:  1*x1 + 2*x2 + 3*x3 + 40*x4 +    x5 +    x6 +    x7 +   x8 +   x9 +    x10 <= 200;
c5:  1*x1 +   x2 +   x3 +    x4 +    x5 +    x6 +    x7 +   x8 +   x9 +    x10 <= 800;
c6:  1*x1 + 2*x2 +   x3 +    x4 +    x5 + 10*x6 +    x7 +   x8 +   x9 +    x10 >= 3;
c7: 20*x1 +   x2 +   x3 +    x4 +  5*x5 +  6*x6 +  7*x7 + 8*x8 + 9*x9 + 10*x10 <= 100;
c8: 10*x1 + 9*x2 + 8*x3 +  7*x4 +  6*x5 +  5*x6 +  4*x7 + 3*x8 + 2*x9 +  1*x10 <= 560;
