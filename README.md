# TDD by Example in Java: Finding Shortest Route

- We need to find the shortest route in a network of routes
- A route is one or more legs
- A leg has a from, to, and an associated value
- The shortest route is a list of one or more legs, if any, with the lowest total value
- Example leg:
	A --1--> B
	A: From
	B: To
	1: Value
	
- Example network of routes:
```
	  --1--> B --2--> 
	A 				C --1--> D
	  -------2------>
```

- Shortest routes for the above network:
```
	A to B: [A --1--> B]
	B to C: [B --2--> C]
	C to D: [C --1--> D]
	B to D: [B --2--> C, C --1--> D]
	A to C: [A --2--> C]
	A to D: [A --2--> C, C --1--> D]
	A to E: []
```
	