(ns advent.day16.promenade)

(def url "src/advent/day16/day16.txt")

(def test-str "s1,x3/4,pe/b")

(defn load-url [url]
  (clojure.string/trim (slurp url)))

(defn prep-data [s]
  (clojure.string/split s #","))

(defn build-program-list [n]
  (mapv (fn [n] (char (+ n 97))) (range n)))

(defn spin
  "Move n number of programs from the end to the front
  s3 on abcde produces cdeab
  "
  [v x]
  (let [[l r] (split-at (- (count v) x) v)]
    (vec (concat r l))))

(defn exchange
  "Exchange the programs at positions a and b"
  [v a b]
  (assoc v b (v a) a (v b)))

(defn partner
  "Swap places of programs named a and b"
  [v a b]
  (exchange v (.indexOf v a) (.indexOf v b)))

(defn char->int [c]
  (Integer/parseInt (str c)))

(defn parse-command-str [s]
  (let [cmd (first (seq s))
        params (.substring s 1)
        [p1 p2] (clojure.string/split params #"/")]
    [cmd p1 p2]))

(defn string->char [s]
  (char (first s)))

(defn process-command-str
  "For a given command string run it's function"
  [v s]
  (let [[cmd p1 p2] (parse-command-str s)]
    (cond
      (= cmd \s) (spin v (char->int p1))
      (= cmd \x) (exchange v (char->int p1) (char->int p2))
      (= cmd \p) (partner v (string->char p1) (string->char p2))
      )
    ))

;; (defn process [n s]
;;   (apply str (reduce process-command-str (build-program-list n) (prep-data s))))
          
(defn process [n s]
  (reduce process-command-str (build-program-list n) (prep-data s)))


#_ (apply str (process 5 test-str))
;; "baedc"

#_ (apply str (process 16 (load-url url)))
;; "lgpkniodmjacfbeh"

(defn run-part1 []
  (apply str (process 16 (load-url url))))
;; "lgpkniodmjacfbeh"
  


;; Part 2
;; Perform the same dance 1 billion times

(defn process2 [programs steps]
  (reduce process-command-str programs steps))

;; TODO: This is very, very slow but does work
(defn process-part2 [max-count]
  (let [dance-moves (prep-data (load-url url))
        fnc (memoize process2)]
    (loop [program-positions (build-program-list 16)
           cnt 0]
      (if (>= cnt max-count)
        program-positions
        (recur (fnc program-positions dance-moves) (inc cnt))))))
    
    
(defn run-part2 []
  (apply str (process-part2  1000000000)))
;; hklecbpnjigoafmd

