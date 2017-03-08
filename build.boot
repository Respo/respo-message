
(set-env!
  :asset-paths #{"assets/"}
  :resource-paths #{"polyfill" "src"}
  :dependencies '[[org.clojure/clojure       "1.8.0"       :scope "provided"]
                  [org.clojure/clojurescript "1.9.473"     :scope "provided"]
                  [adzerk/boot-cljs          "1.7.228-1"   :scope "provided"]
                  [adzerk/boot-reload        "0.4.13"      :scope "provided"]
                  [cirru/boot-stack-server   "0.1.30"      :scope "provided"]
                  [andare                    "0.5.0"       :scope "provided"]
                  [cumulo/shallow-diff       "0.1.3"       :scope "provided"]
                  [fipp                      "0.6.9"       :scope "provided"]
                  [respo                     "0.3.39"      :scope "provided"]
                  [respo/ui                  "0.1.9"       :scope "provided"]
                  [mvc-works/hsl             "0.1.2"]])

(require '[adzerk.boot-cljs   :refer [cljs]]
         '[adzerk.boot-reload :refer [reload]])

(def +version+ "0.1.3")

(task-options!
  pom {:project     'respo/message
       :version     +version+
       :description "Message component on the right top"
       :url         "https://github.com/Respo/respo-message"
       :scm         {:url "https://github.com/Respo/respo-message"}
       :license     {"MIT" "http://opensource.org/licenses/mit-license.php"}})

(deftask dev []
  (comp
    (watch)
    (reload :on-jsload 'respo-message.main/on-jsload!
            :cljs-asset-path ".")
    (cljs :compiler-options {:language-in :ecmascript5})
    (target)))

(deftask build-advanced []
  (comp
    (cljs :optimizations :advanced
          :compiler-options {:language-in :ecmascript5
                             :pseudo-names true
                             :static-fns true
                             :parallel-build true
                             :optimize-constants true
                             :source-map true})
    (target)))

(deftask build []
  (comp
    (pom)
    (jar)
    (install)
    (target)))

(deftask deploy []
  (set-env!
    :repositories #(conj % ["clojars" {:url "https://clojars.org/repo/"}]))
  (comp
    (build)
    (push :repo "clojars" :gpg-sign (not (.endsWith +version+ "-SNAPSHOT")))))
