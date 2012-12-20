var x1;
var x2 integer;

maximize f1: 10*x1 + x2;
maximize f2: x1 + 10*x2;

subject to
c1: x1 + x2 <= 10;
c2: x1 >= 0;
c3: x2 >= 0;
