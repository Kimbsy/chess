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
        (map c/fen->board)
        (map print-move)))
  nil)

(defn output-game
  "Write clean concatenated FEN-like board strings move by move to stdout."
  [file]
  (doseq [move (->> file
                    clojure.java.io/resource
                    clojure.java.io/reader
                    line-seq
                    (map c/fen->board)
                    (map #(apply str %)))]
    (println move)
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
  [moves]
  (loop [b c/initial-fen
         n 0]
    (when (< n moves)
      (newline)
      (p/print-board b)
      (recur (sf/apply-uci b (sf/best-move b)) (inc n)))))

(defn -main
  [& args]
  (output-game "games/001_kasparov_topalov_1999.fen"))
