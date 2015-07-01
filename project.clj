(defproject crud-talk "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [ring "1.3.0"]
                 [bidi "1.20.0"]
                 [yesql "0.4.0" :exclusions [instaparse]]
                 [instaparse "1.4.0"]
                 [org.xerial/sqlite-jdbc "3.7.2"]
                 [hiccup "1.0.5"]
                 [erinite/template "0.2.0"]]
  :plugins [[lein-ring "0.9.6"]]
  :ring {:handler crud-talk.core/handler}
  :resource-paths ["resources"])
