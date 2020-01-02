(ns advent.core
  (:require [advent.day01.captcha :as day1-part1]
            [advent.day01.captcha2 :as day1-part2]
            [advent.day02.checksum :as day2]
            [advent.day03.spiral :as day3]
            [advent.day04.passphrase :as day4]
            [advent.day05.trampoline :as day5]
            [advent.day06.reallocation :as day6]
            [advent.day07.circus :as day7]
            [advent.day08.registers :as day8]
            [advent.day09.stream :as day9]
            [advent.day10.knothash :as day10]
            [advent.day11.hexed :as day11]
            [advent.day12.plumber :as day12]
            [advent.day13.scanner :as day13]
            [advent.day14.defrag :as day14]
            [advent.day15.dueling :as day15]
            [advent.day15.dueling2 :as day152]
            [advent.day16.promenade :as day16]
            [advent.day17.spinlock :as day17]
            [advent.day18.duet :as day18]
            [advent.day18.duet3 :as day18-part2]
            [advent.day19.tubes :as day19]
            [advent.day20.swarm :as day20]
            [advent.day21.fractal :as day21]
            [advent.day22.virus :as day22]
            [advent.day23.coprocessor :as day23]
            [advent.day24.moat :as day24]
            [advent.day25.halting :as day25])
  (:gen-class))

(defn -main [& args]
  (println "Advent of Code 2017")
  (println "--------------------------------------------------")
  (println (format "Day  1 / Part 1 : %d" (day1-part1/run)))
  (println (format "Day  1 / Part 2 : %d" (day1-part2/run)))

  (println (format "Day  2 / Part 1 : %d" (day2/run-part1)))
  (println (format "Day  2 / Part 2 : %d" (day2/run-part2)))

  (println (format "Day  3 / Part 1 : %d" (day3/run-part1)))
  (println (format "Day  3 / Part 2 : %d" (day3/run-part2)))

  (println (format "Day  4 / Part 1 : %d" (day4/run-part1)))
  (println (format "Day  4 / Part 2 : %d" (day4/run-part2)))

  (println (format "Day  5 / Part 1 : %d" (day5/run-part1)))
  (println (format "Day  5 / Part 2 : %d" (day5/run-part2))) 

  (println (format "Day  6 / Part 1 : %d" (day6/run-part1)))
  (println (format "Day  6 / Part 2 : %d" (day6/run-part2)))
  
  (println (format "Day  7 / Part 1 : %s" (day7/run-part1)))
  (println (format "Day  7 / Part 2 : %d" (day7/run-part2)))

  (println (format "Day  8 / Part 1 : %d" (day8/run-part1)))
  (println (format "Day  8 / Part 2 : %d" (day8/run-part2)))

  (println (format "Day  9 / Part 1 : %d" (day9/run-part1)))
  (println (format "Day  9 / Part 2 : %d" (day9/run-part2)))

  (println (format "Day 10 / Part 1 : %d" (day10/run-part1)))
  (println (format "Day 10 / Part 2 : %s" (day10/run-part2)))
  
  (println (format "Day 11 / Part 1 : %d" (day11/run-part1)))
  (println (format "Day 11 / Part 2 : %s" (day11/run-part2)))

  (println (format "Day 13 / Part 1 : %d" (day13/run-part1)))
  (println (format "Day 13 / Part 2 : %d" (day13/run-part2)))

  (println (format "Day 14 / Part 1 : %d" (day14/run-part1)))
  (println (format "Day 14 / Part 2 : %d" (day14/run-part2)))

  (println (format "Day 15 / Part 1 : %d" (day15/run-part1)))
  (println (format "Day 15 / Part 2 : %d" (day152/run-part2)))

  (println (format "Day 16 / Part 1 : %s" (day16/run-part1)))
  (println (format "Day 16 / Part 2 : %s" (day16/run-part2)))

  (println (format "Day 17 / Part 1 : %s" (day17/run-part1)))
  (println (format "Day 17 / Part 2 : %s" (day17/run-part2)))

  (println (format "Day 18 / Part 1 : %s" (day18/run-part1)))
  (println (format "Day 18 / Part 2 : %d" 7493))   ;; Run (day18-part2/run-part2) from with the REPL

  (println (format "Day 19 / Part 1 : %s" (day19/run-part1)))
  (println (format "Day 19 / Part 2 : %d" (day19/run-part2)))

  (println (format "Day 20 / Part 1 : %d" (day20/run-part1)))
  (println (format "Day 20 / Part 2 : %d" (day20/run-part2)))

  (println (format "Day 21 / Part 1 : %d" (day21/run-part1)))
  (println (format "Day 21 / Part 2 : %d" (day21/run-part2)))
  
  (println (format "Day 22 / Part 1 : %d" (day22/run-part1)))
  (println (format "Day 22 / Part 2 : %d" (day22/run-part2)))

  (println (format "Day 23 / Part 1 : %d" (day23/run-part1)))
  (println (format "Day 23 / Part 2 : %d" (day23/run-part2)))
  
  (println (format "Day 24 / Part 1 : %d" (day24/run-part1)))
  (println (format "Day 24 / Part 2 : %d" (day24/run-part2)))

  (println (format "Day 25 / Part 1 : %d" (day25/run-part1)))
  (println (format "Day 25 / Part 2 : %s" (day25/run-part2))))
