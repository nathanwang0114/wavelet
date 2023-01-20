import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    ArrayList<String> words = new ArrayList<String>();
    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            if (words.size() == 0) {
                return "word list is empty";
            }
            else {
                return words.toString();
            }
        } 
        else if (url.getPath().contains("/search")) {
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {
                String keyword = (parameters[1]);
                ArrayList<String> filteredWords = new ArrayList<String>();
                for(String word: words) {
                    if(word.indexOf(keyword) >= 0) {
                        filteredWords.add(word);
                    }
                }
                if(filteredWords.size() == 0) {
                    return String.format("no words in the word list have %s", keyword);
                }
                return String.format("words with %s in word list are: %s", keyword, filteredWords.toString());
            }
            return "404 Not Found!";
        }
        else {
            if (url.getPath().contains("/add")) {
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("s")) {
                    words.add(parameters[1]);
                    return String.format("word list added %s! It's now %s", parameters[1], words.toString());
                }
            }
            return "404 Not Found!";
        }
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}