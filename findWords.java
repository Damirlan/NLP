package nlp.testproject;
import java.io.*;
import java.util.*;

import java.lang.Object;
import ru.textanalysis.tawt.graphematic.parser.text.GParserImpl;
import ru.textanalysis.tawt.graphematic.parser.text.GraphematicParser;
import ru.textanalysis.tawt.jmorfsdk.JMorfSdk;
import ru.textanalysis.tawt.jmorfsdk.JMorfSdkFactory;
import ru.textanalysis.tawt.ms.model.jmorfsdk.DerivativeForm;
import ru.textanalysis.tawt.ms.model.jmorfsdk.Form;
import ru.textanalysis.tawt.sp.api.SyntaxParser;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import ru.textanalysis.tawt.ms.external.sp.BearingPhraseExt;
import ru.textanalysis.tawt.sp.api.SyntaxParser;


public class findWords {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input.txt");

        String findword = "футбольный";
        int countCoincidence = 0;

        Scanner scanner = new Scanner(file);

        List<String> wordsFromAbbr = getAbbr(scanner);
        //System.out.println(wordsFromAbbr);

        List<String> wordsFromText = getText(scanner);
        //System.out.println(wordsFromText);

        System.out.println(countOfMatches(wordsFromAbbr, wordsFromText));

    }
    public static List<String> getAbbr(Scanner scanner) throws FileNotFoundException {
        String abbr = scanner.nextLine();

        GraphematicParser parser = new GParserImpl();

        List<List<String>> listBasicPhase = parser.parserSentence(abbr);

        List<String> wordsAbbr = new ArrayList<>();

        //генерация списка слов из текста
        for(List<String> words : listBasicPhase) {
            wordsAbbr.addAll(words);
        }

        return generationNormList(wordsAbbr.subList(2, wordsAbbr.size()));
    }

    public static List<String> getText(Scanner scanner)throws FileNotFoundException {
        //String abbr = scanner.nextLine();
        String text = scanner.nextLine();

        GraphematicParser parser = new GParserImpl();

        List<List<String>> listBasicPhase = parser.parserSentence(text);

        List<String> wordsFromText = new ArrayList<>();

        //генерация списка слов из текста
        for(List<String> words : listBasicPhase) {
            wordsFromText.addAll(words);
        }
        return generationNormList(wordsFromText);
    }
    public static List<String> generationNormList(List<String> list) {

        List<String> normList = new ArrayList<>();
        List<String> normForms = new ArrayList<>();

        JMorfSdk jMorfSdk = JMorfSdkFactory.loadFullLibrary();

        for (String word : list) {
            try {
                jMorfSdk.getOmoForms(word.toLowerCase()).forEach((form) -> {
                    normForms.add(form.getInitialFormString());
                });
                normList.add(normForms.get(0));
                normForms.clear();
            } catch (Exception e) {
                System.out.println("ошибочка");
            }

        }
        //jMorfSdk.finish();
        return normList;
    }

    public static Map<String,Integer> countOfMatches(List<String> wordsFromAbbr, List<String> wordsFromText) {
        Map<String,Integer> countMatches = new HashMap<String,Integer>();

        for (String wordAbbr: wordsFromAbbr) {
            int count = 0;
            for (String wordText: wordsFromText) {
                if (wordAbbr.equals(wordText)) {
                    count += 1;
                }
            }
            countMatches.put(wordAbbr, count);
        }
        return countMatches;
    }
}
