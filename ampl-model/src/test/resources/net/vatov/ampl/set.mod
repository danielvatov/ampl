set A;
set B dimen 3;
set C = {"a", "b"};
set D = {};
set E default A;
set H default {3,4};

/*validate when assign data*/
set NEW within C;
set NEW1 within {1,2};

/*
param a = C within A;
param b = H within {1,2,3,4,5};
param c = 4 in H;
param d = "a" in C;
*/

/*precedence*/
set K = A union B inter C diff D;
set L = (A union (B inter C)) diff D;

param start := 1990;
param end := 2020;
param step := 5;
set YEARS = 1990 .. 2020 by 5;
set YEARS1 = start .. end by step;

/*

param cardinality = card(C);

param rate {C} > 0;
param avail {1..10 by 2} >= 3;

var var_rate {C} > 0;
var var_avail {1..10 by 2} >= 3;

param f_min {C} >= 0;
param f_max {the_index in C} >= f_min[the_index];
*/
/*Specifing condition defines new set*/
/*
set G = {j in C: f_max[j] - f_min[j) < 1}
set F = {i in C : i in H or f_min[i] > O}
*/

set M ordered = {1,2,3,4,5};
set O circular = {"a", "b", "c", "d", "e", "f", "g"};

/*
param m1 = sum {i in M: i>1} (prev(i) + first(t) + last(t) + next(t));
param o1 = member(3, M);
param o2 = prev("c", M, 2);
param o3 = prevw("c", M, 2);
param o4 = next("c", M, 2);
param o5 = nextw("c", M, 2);
param o6 = ord("c", M);
param o7 = ord0("z", M);
*/

/*
???
interval [a, b]
interval (a, b]
interval [a, b)
interval (a, b)
integer [a, b]
integer (a, b]
integer [a, b)
integer (a, b)
p 107
*/
