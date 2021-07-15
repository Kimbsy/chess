(ns chess.core
  (:require [chess.common :as c]
            [chess.printing :as p]
            [chess.stockfish :as sf])
  (:gen-class))

(defn print-move
  [fen]
  (Thread/sleep 500)
  (dotimes [_ 80]
    (newline))
  (p/print-board fen)
  nil)

(defn print-game
  [file]
  (doall
   (->> file
        clojure.java.io/resource
        clojure.java.io/reader
        line-seq
        (map print-move)))
  nil)

(defn output-board
  "Write clean concatenated FEN-like board string to stdout."
  [fen]
  (let [state (:state (c/fen->board fen))]
    (->> state
         (map #(apply str %))
         (apply str)
         println)))

(defn output-game
  " move by move to stdout."
  [file]
  (doseq [move (->> file
                    clojure.java.io/resource
                    clojure.java.io/reader
                    line-seq
                    )]
    (output-board move)
    (Thread/sleep 500)))

(def top-10-games
  ["games/001_kasparov_topalov_1999.fen"
   "games/002_byrne_fischer_1956.fen"
   "games/003_beliavsky_nunn_1985.fen"
   "games/004_ivanchuk_yusupov_1991.fen"
   "games/005_rotlewi_rubinstein_1907.fen"
   "games/006_polugaevsky_nezhmetdinov_1958.fen"
   "games/007_bagirov_gufeld_1973.fen"
   "games/008_tal_larsen_1965.fen"
   "games/009_krasenkow_nakamura_2007.fen"
   "games/010_anderssen_kieseritzky_1851.fen"])

(defn auto-play
  [moves output-format]
  (loop [b c/initial-fen
         n 0]
    (when (< n moves)
      (if (= :stdout output-format)
        (do (output-board b)
            (Thread/sleep 300))
        (p/print-board b))
      (recur (sf/apply-uci b (sf/best-move b)) (inc n)))))

(defn loop-auto
  [output-format]
  (loop [b    c/initial-fen
         n    0
         prev nil]
    (let [new (sf/apply-uci b (sf/best-move b))]
      (if (= prev new)
        (do
          (Thread/sleep 1000)
          (recur c/initial-fen 0 nil))
        (do
          (if (= :stdout output-format)
            (do (output-board b)
                (Thread/sleep 300))
            (p/print-board b))
          (recur new (inc n) b))))))

(defn -main
  [& args]
  (loop-auto :stdout)
  #_(auto-play 40 :stdout)
  #_(output-game "games/001_kasparov_topalov_1999.fen"))
