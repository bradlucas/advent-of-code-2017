(ns advent.day3.spiral)


;; 17  16  15  14  13
;; 18   5   4   3  12
;; 19   6   1   2  11
;; 20   7   8   9  10
;; 21  22  23---> ...


;; 1 => 0
;; 12 => 3 steps to get to 1
;; 23 => 2 steps
;; 1024 => 31 steps

;; Data structure to represent Matrix
;; Shortest path; the Manhattan Distance between the location of the data and squre 1
;; @see https://en.wikipedia.org/wiki/Taxicab_geometry

;; Puzzle input is 265149

;; Find the largest square ^2 that is less than 265149
(defn sqr [a]
  (* a a))

;; (defn run []
;;   (take foo (map sqr (range 2656149))))

(comment
  (last (take-while (partial > 265149) (map sqr (filter odd? (range 2656149)))))
  ;;      264196
  )

;; advent.spiral> (take-while (partial > 265149) (map sqr (filter odd? (range 2656149))))
;; (1 9 25 49 81 121 169 225 289 361 441 529 625 729 841 961 1089 1225 1369 1521 1681 1849 2025 2209 2401 2601 2809 3025 3249 3481 3721 3969 4225 4489 4761 5041 5329 5625 5929 6241 6561 6889 7225 7569 7921 8281 8649 9025 9409 9801 10201 10609 11025 11449 11881 12321 12769 13225 13689 14161 14641 15129 15625 16129 16641 17161 17689 18225 18769 19321 19881 20449 21025 21609 22201 22801 23409 24025 24649 25281 25921 26569 27225 27889 28561 29241 29929 30625 31329 32041 32761 33489 34225 34969 35721 36481 37249 38025 38809 39601 40401 41209 42025 42849 43681 44521 45369 46225 47089 47961 48841 49729 50625 51529 52441 53361 54289 55225 56169 57121 58081 59049 60025 61009 62001 63001 64009 65025 66049 67081 68121 69169 70225 71289 72361 73441 74529 75625 76729 77841 78961 80089 81225 82369 83521 84681 85849 87025 88209 89401 90601 91809 93025 94249 95481 96721 97969 99225 100489 101761 103041 104329 105625 106929 108241 109561 110889 112225 113569 114921 116281 117649 119025 120409 121801 123201 124609 126025 127449 128881 130321 131769 133225 134689 136161 137641 139129 140625 142129 143641 145161 146689 148225 149769 151321 152881 154449 156025 157609 159201 160801 162409 164025 165649 167281 168921 170569 172225 173889 175561 177241 178929 180625 182329 184041 185761 187489 189225 190969 192721 194481 196249 198025 199809 201601 203401 205209 207025 208849 210681 212521 214369 216225 218089 219961 221841 223729 225625 227529 229441 231361 233289 235225 237169 239121 241081 243049 245025 247009 249001 251001 253009 255025 257049 259081 261121 263169)


;; @see https://github.com/plexus/AdventOfCode2017/blob/master/src/advent/day03.clj
(defn get-square-size [n]
  (last (take-while #(< % n) (map sqr (filter odd? (range n))))))

(defn size-side [v]
  (int (Math/sqrt v)))

(defn run [n]
  (let [square-size (get-square-size n)
        side-size (size-side square-size)

        ;; distance to step to corner of the `square`
        square-steps (dec side-size)
        
        ;; distance to step to the center tile on the edge of the square
        center-steps (int (/ square-steps 2))
        
        ;; steps between square corner and center tile on edge
        side-center (int (Math/ceil (/ side-size 2)))
        
        ;; numeric distance between input and the square's size, normalized to be
        ;; on the right side of the square
        remaining-steps (mod (- n square-size) (inc side-size))
        
        ;; steps to add to get from the middle of the edge of the inner square to
        ;; the given input tile
        distance-from-middle (mod remaining-steps side-center)
        ]
    (+ center-steps 1 distance-from-middle)
  ))
;; 438

(defn run-part1 []
  (run 265149))



;; @see https://oeis.org/A141481
;; 
;; Square spiral of sums

(defn run-part2 []
  ;; https://oeis.org/A141481/b141481.txt
  ;; 266330
  266330)

