import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
//import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//import java.util.LinkedHashMap;
//import java.util.stream.Collectors;
import java.util.stream.Collectors;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

/**
 * NounSearcher (Morphological Analysis)
 * @author junyoung0322
 *
 */
public class NounSearcher {

	public static void main(String[] args) {
		// ファイル経路
		String sFilePath = "\\file\\Data.txt";
		// テキストデータ格納用オブジェクト
		StringBuilder sText = new StringBuilder();
		// 分析結果格納用オブジェクト
//		ArrayList<String> lNouns = new ArrayList<String>();

		// テキストファイル指定
		File ofile = new File(System.getProperty("user.dir") + sFilePath);
		try {
			// テキストファイルの内容取得
			BufferedReader reader = new BufferedReader(new FileReader(ofile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sText.append(line);
			}
			reader.close();
		} catch (FileNotFoundException fnf) {
			fnf.printStackTrace();
		} catch( IOException e) {
			e.printStackTrace();
		}

		// テキスト形態素解析開始
		Tokenizer oTokenizer = Tokenizer.builder().build();
		// テキストデータをセット
		List<Token> lTokens = oTokenizer.tokenize(sText.toString());


		// 形態素解析結果から名詞の絞り込み
		List<String> lNouns = lTokens.stream()
				.filter(t -> t.getAllFeatures().split(",")[0].equals("名詞"))
				.map(Token::getSurfaceForm)
				.collect(Collectors.toList());
/////////////////////////////////////////////////////////
//		for (Token oToken: lTokens) {
//			String[] sForm = oToken.getAllFeatures().split(",");
//			// 名詞のみを抽出
//			if (sForm[0].equals("名詞")) {
//				lNouns.add(oToken.getSurfaceForm());
//			}
//		}
////////////////////////////////////////////////////////

		// 同一名詞別にをグルーピングしてその重複回数をマップに格納
		Map<String, Long> mResult = lNouns.stream()
				.collect(groupingBy(identity(), counting()));
////////////////////////////////////////////////////////
//		Map<String, Integer> mResult = lNoun.stream().collect(Collectors.toMap(k -> k, v -> 1, (v1, v2) -> v1 + v2, LinkedHashMap::new));
////////////////////////////////////////////////////////

		// カウント数を降順に表示
		mResult.entrySet().stream()
		.sorted(Map.Entry.<String, Long>comparingByValue().reversed())
		.forEach(x -> System.out.printf("%s - %d%n", x.getKey(), x.getValue()));
////////////////////////////////////////////////////////
//		.sorted(java.util.Collections.reverseOrder(java.util.Map.Entry.comparingByValue()))
//		.forEach(x -> System.out.println(x.getKey() + " - " + x.getValue()));
////////////////////////////////////////////////////////
	}
}
