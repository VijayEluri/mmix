/*
 * Created on 2005-5-5
 *


 */
package eddie.wu.linkedblock;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger
;

import eddie.wu.domain.Constant;
import eddie.wu.manual.GMDGoManual;
import eddie.wu.manual.LoadGMDGoManual;

/**
 * @deprecated
 * @author eddie
 * 
 */
public class TestLoadGoManual extends TestCase {
	public static final int MOVES_THRESHOLD = 100;
	private static final Logger log = Logger.getLogger(TestLoadGoManual.class);
	private static final String rootDir = Constant.rootDir;// "doc/围棋打谱软件/";

	public void testOneFromMultiGoManual() {
		byte[] temp = new LoadGMDGoManual(rootDir).loadSingleGoManual()
				.getMoves();
		log.debug(temp.toString());
		byte[] temp1 = new LoadGMDGoManual(rootDir).loadOneFromLib0(0)
				.getMoves();
		assertEquals(temp.length, temp1.length);
		for (int i = 0; i < temp.length; i++) {
			assertEquals(temp[i], temp1[i]);
		}
		temp1 = new LoadGMDGoManual(rootDir).loadOneFromLib0(1).getMoves();
		assertEquals(temp1.length, 247 * 2);
		temp1 = new LoadGMDGoManual(rootDir).loadOneFromLib0(2).getMoves();
		assertEquals(temp1.length, 223 * 2);
		temp1 = new LoadGMDGoManual(rootDir).loadOneFromLib0(3).getMoves();
		assertEquals(temp1.length, 349 * 2);// 93
		temp1 = new LoadGMDGoManual(rootDir).loadOneFromLib0(4).getMoves();
		assertEquals(temp1.length, 183 * 2);

		temp1 = new LoadGMDGoManual(rootDir).loadOneFromLib0(5).getMoves();
		assertEquals(temp1.length, 203 * 2);
		temp1 = new LoadGMDGoManual(rootDir).loadOneFromLib0(6).getMoves();
		assertEquals(temp1.length, 152 * 2);
		temp1 = new LoadGMDGoManual(rootDir).loadOneFromLib0(7).getMoves();
		assertEquals(temp1.length, 245 * 2);

		temp1 = new LoadGMDGoManual(rootDir).loadOneFromLib0(8).getMoves();
		assertEquals(temp1.length, 270 * 2);// 14

	}

	/**
	 * failed because lib1 is lost.
	 */
	public void testLoadMultiGoManual() {
		List<GMDGoManual> list = new LoadGMDGoManual(rootDir)
				.loadMultiGoManualFromLib0();
		assertEquals(9, list.size());
		list = new LoadGMDGoManual(rootDir).loadMultiGoManual(1);
		assertEquals(1000, list.size());
		int[] tempLength = new int[1000];
		// in.available()=1024000
		// ii=0shoushu=309ii=1shoushu=294ii=2shoushu=250ii=3shoushu=279ii=4shoushu=280ii=5shoushu=260ii=6shoushu=258ii=7shoushu=269ii=8shoushu=268ii=9shoushu=257ii=10shoushu=262ii=11shoushu=285ii=12shoushu=220ii=13shoushu=254ii=14shoushu=253ii=15shoushu=236ii=16shoushu=256ii=17shoushu=258ii=18shoushu=256ii=19shoushu=259ii=20shoushu=265ii=21shoushu=260ii=22shoushu=231ii=23shoushu=223ii=24shoushu=246ii=25shoushu=245ii=26shoushu=215ii=27shoushu=279ii=28shoushu=246ii=29shoushu=230ii=30shoushu=250ii=31shoushu=267ii=32shoushu=264ii=33shoushu=244ii=34shoushu=252ii=35shoushu=288ii=36shoushu=299ii=37shoushu=249ii=38shoushu=222ii=39shoushu=284ii=40shoushu=269ii=41shoushu=262ii=42shoushu=254ii=43shoushu=249ii=44shoushu=269ii=45shoushu=220ii=46shoushu=226ii=47shoushu=252ii=48shoushu=232ii=49shoushu=237ii=50shoushu=196ii=51shoushu=267ii=52shoushu=226ii=53shoushu=248ii=54shoushu=216ii=55shoushu=295ii=56shoushu=287ii=57shoushu=255ii=58shoushu=232ii=59shoushu=224ii=60shoushu=260ii=61shoushu=268ii=62shoushu=206ii=63shoushu=226ii=64shoushu=220ii=65shoushu=214ii=66shoushu=221ii=67shoushu=270ii=68shoushu=229ii=69shoushu=264ii=70shoushu=226ii=71shoushu=256ii=72shoushu=266ii=73shoushu=293ii=74shoushu=265ii=75shoushu=231ii=76shoushu=231ii=77shoushu=298ii=78shoushu=280ii=79shoushu=244ii=80shoushu=218ii=81shoushu=258ii=82shoushu=275ii=83shoushu=251ii=84shoushu=211ii=85shoushu=234ii=86shoushu=281ii=87shoushu=287ii=88shoushu=259ii=89shoushu=213ii=90shoushu=264ii=91shoushu=232ii=92shoushu=204ii=93shoushu=246ii=94shoushu=197ii=95shoushu=302ii=96shoushu=250ii=97shoushu=241ii=98shoushu=250ii=99shoushu=191ii=100shoushu=234ii=101shoushu=235ii=102shoushu=286ii=103shoushu=252ii=104shoushu=246ii=105shoushu=279ii=106shoushu=275ii=107shoushu=206ii=108shoushu=258ii=109shoushu=227ii=110shoushu=255ii=111shoushu=194ii=112shoushu=277ii=113shoushu=262ii=114shoushu=270ii=115shoushu=222ii=116shoushu=218ii=117shoushu=282ii=118shoushu=238ii=119shoushu=229ii=120shoushu=250ii=121shoushu=227ii=122shoushu=248ii=123shoushu=215ii=124shoushu=247ii=125shoushu=267ii=126shoushu=197ii=127shoushu=218ii=128shoushu=215ii=129shoushu=296ii=130shoushu=285ii=131shoushu=199ii=132shoushu=200ii=133shoushu=236ii=134shoushu=259ii=135shoushu=251ii=136shoushu=199ii=137shoushu=218ii=138shoushu=254ii=139shoushu=183ii=140shoushu=303ii=141shoushu=245ii=142shoushu=256ii=143shoushu=265ii=144shoushu=186ii=145shoushu=252ii=146shoushu=230ii=147shoushu=177ii=148shoushu=249ii=149shoushu=254ii=150shoushu=158ii=151shoushu=201ii=152shoushu=246ii=153shoushu=272ii=154shoushu=268ii=155shoushu=240ii=156shoushu=142ii=157shoushu=201ii=158shoushu=259ii=159shoushu=256ii=160shoushu=194ii=161shoushu=164ii=162shoushu=279ii=163shoushu=190ii=164shoushu=199ii=165shoushu=238ii=166shoushu=185ii=167shoushu=150ii=168shoushu=162ii=169shoushu=247ii=170shoushu=152ii=171shoushu=240ii=172shoushu=200ii=173shoushu=262ii=174shoushu=246ii=175shoushu=238ii=176shoushu=220ii=177shoushu=159ii=178shoushu=243ii=179shoushu=194ii=180shoushu=211ii=181shoushu=258ii=182shoushu=143ii=183shoushu=188ii=184shoushu=191ii=185shoushu=260ii=186shoushu=251ii=187shoushu=202ii=188shoushu=265ii=189shoushu=271ii=190shoushu=187ii=191shoushu=186ii=192shoushu=156ii=193shoushu=207ii=194shoushu=278ii=195shoushu=184ii=196shoushu=152ii=197shoushu=246ii=198shoushu=240ii=199shoushu=296ii=200shoushu=296ii=201shoushu=271ii=202shoushu=220ii=203shoushu=262ii=204shoushu=269ii=205shoushu=240ii=206shoushu=305ii=207shoushu=145ii=208shoushu=217ii=209shoushu=204ii=210shoushu=330ii=211shoushu=249ii=212shoushu=148ii=213shoushu=157ii=214shoushu=162ii=215shoushu=258ii=216shoushu=181ii=217shoushu=220ii=218shoushu=145ii=219shoushu=186ii=220shoushu=215ii=221shoushu=167ii=222shoushu=295ii=223shoushu=151ii=224shoushu=283ii=225shoushu=159ii=226shoushu=163ii=227shoushu=202ii=228shoushu=201ii=229shoushu=278ii=230shoushu=235ii=231shoushu=257ii=232shoushu=258ii=233shoushu=227ii=234shoushu=183ii=235shoushu=161ii=236shoushu=179ii=237shoushu=225ii=238shoushu=139ii=239shoushu=155ii=240shoushu=249ii=241shoushu=224ii=242shoushu=266ii=243shoushu=225ii=244shoushu=147ii=245shoushu=137ii=246shoushu=257ii=247shoushu=164ii=248shoushu=208ii=249shoushu=283ii=250shoushu=269ii=251shoushu=151ii=252shoushu=189ii=253shoushu=280ii=254shoushu=299ii=255shoushu=233ii=256shoushu=171ii=257shoushu=186ii=258shoushu=218ii=259shoushu=139ii=260shoushu=172ii=261shoushu=142ii=262shoushu=282ii=263shoushu=185ii=264shoushu=144ii=265shoushu=162ii=266shoushu=167ii=267shoushu=264ii=268shoushu=176ii=269shoushu=147ii=270shoushu=217ii=271shoushu=169ii=272shoushu=243ii=273shoushu=169ii=274shoushu=273ii=275shoushu=251ii=276shoushu=204ii=277shoushu=165ii=278shoushu=141ii=279shoushu=146ii=280shoushu=154ii=281shoushu=135ii=282shoushu=208ii=283shoushu=274ii=284shoushu=124ii=285shoushu=213ii=286shoushu=175ii=287shoushu=184ii=288shoushu=154ii=289shoushu=274ii=290shoushu=265ii=291shoushu=206ii=292shoushu=164ii=293shoushu=225ii=294shoushu=159ii=295shoushu=213ii=296shoushu=196ii=297shoushu=271ii=298shoushu=333ii=299shoushu=184ii=300shoushu=180ii=301shoushu=274ii=302shoushu=168ii=303shoushu=205ii=304shoushu=203ii=305shoushu=210ii=306shoushu=263ii=307shoushu=216ii=308shoushu=219ii=309shoushu=245ii=310shoushu=152ii=311shoushu=134ii=312shoushu=218ii=313shoushu=285ii=314shoushu=241ii=315shoushu=129ii=316shoushu=245ii=317shoushu=126ii=318shoushu=155ii=319shoushu=271ii=320shoushu=276ii=321shoushu=266ii=322shoushu=226ii=323shoushu=200ii=324shoushu=195ii=325shoushu=237ii=326shoushu=183ii=327shoushu=269ii=328shoushu=283ii=329shoushu=310ii=330shoushu=219ii=331shoushu=244ii=332shoushu=259ii=333shoushu=174ii=334shoushu=279ii=335shoushu=294ii=336shoushu=174ii=337shoushu=260ii=338shoushu=244ii=339shoushu=172ii=340shoushu=277ii=341shoushu=249ii=342shoushu=245ii=343shoushu=220ii=344shoushu=303ii=345shoushu=269ii=346shoushu=293ii=347shoushu=220ii=348shoushu=279ii=349shoushu=200ii=350shoushu=189ii=351shoushu=192ii=352shoushu=280ii=353shoushu=182ii=354shoushu=243ii=355shoushu=230ii=356shoushu=233ii=357shoushu=250ii=358shoushu=267ii=359shoushu=256ii=360shoushu=259ii=361shoushu=267ii=362shoushu=295ii=363shoushu=187ii=364shoushu=325ii=365shoushu=282ii=366shoushu=215ii=367shoushu=221ii=368shoushu=230ii=369shoushu=204ii=370shoushu=195ii=371shoushu=245ii=372shoushu=185ii=373shoushu=239ii=374shoushu=219ii=375shoushu=292ii=376shoushu=254ii=377shoushu=284ii=378shoushu=128ii=379shoushu=258ii=380shoushu=171ii=381shoushu=167ii=382shoushu=257ii=383shoushu=187ii=384shoushu=160ii=385shoushu=292ii=386shoushu=294ii=387shoushu=199ii=388shoushu=216ii=389shoushu=241ii=390shoushu=263ii=391shoushu=280ii=392shoushu=288ii=393shoushu=243ii=394shoushu=238ii=395shoushu=267ii=396shoushu=276ii=397shoushu=184ii=398shoushu=254ii=399shoushu=194ii=400shoushu=276ii=401shoushu=254ii=402shoushu=194ii=403shoushu=186ii=404shoushu=275ii=405shoushu=254ii=406shoushu=190ii=407shoushu=185ii=408shoushu=271ii=409shoushu=246ii=410shoushu=253ii=411shoushu=206ii=412shoushu=216ii=413shoushu=274ii=414shoushu=265ii=415shoushu=177ii=416shoushu=194ii=417shoushu=230ii=418shoushu=234ii=419shoushu=189ii=420shoushu=251ii=421shoushu=265ii=422shoushu=290ii=423shoushu=258ii=424shoushu=291ii=425shoushu=227ii=426shoushu=234ii=427shoushu=228ii=428shoushu=233ii=429shoushu=243ii=430shoushu=257ii=431shoushu=259ii=432shoushu=183ii=433shoushu=233ii=434shoushu=224ii=435shoushu=227ii=436shoushu=176ii=437shoushu=268ii=438shoushu=234ii=439shoushu=182ii=440shoushu=256ii=441shoushu=208ii=442shoushu=273ii=443shoushu=245ii=444shoushu=242ii=445shoushu=265ii=446shoushu=261ii=447shoushu=223ii=448shoushu=273ii=449shoushu=252ii=450shoushu=183ii=451shoushu=241ii=452shoushu=256ii=453shoushu=265ii=454shoushu=269ii=455shoushu=237ii=456shoushu=247ii=457shoushu=283ii=458shoushu=232ii=459shoushu=245ii=460shoushu=228ii=461shoushu=260ii=462shoushu=180ii=463shoushu=211ii=464shoushu=226ii=465shoushu=221ii=466shoushu=289ii=467shoushu=204ii=468shoushu=233ii=469shoushu=280ii=470shoushu=237ii=471shoushu=256ii=472shoushu=203ii=473shoushu=245ii=474shoushu=272ii=475shoushu=247ii=476shoushu=181ii=477shoushu=179ii=478shoushu=215ii=479shoushu=251ii=480shoushu=275ii=481shoushu=274ii=482shoushu=221ii=483shoushu=187ii=484shoushu=181ii=485shoushu=267ii=486shoushu=257ii=487shoushu=181ii=488shoushu=255ii=489shoushu=282ii=490shoushu=254ii=491shoushu=305ii=492shoushu=267ii=493shoushu=319ii=494shoushu=187ii=495shoushu=297ii=496shoushu=191ii=497shoushu=258ii=498shoushu=220ii=499shoushu=246ii=500shoushu=196ii=501shoushu=220ii=502shoushu=306ii=503shoushu=186ii=504shoushu=298ii=505shoushu=263ii=506shoushu=186ii=507shoushu=230ii=508shoushu=233ii=509shoushu=181ii=510shoushu=239ii=511shoushu=182ii=512shoushu=224ii=513shoushu=273ii=514shoushu=253ii=515shoushu=255ii=516shoushu=222ii=517shoushu=226ii=518shoushu=174ii=519shoushu=286ii=520shoushu=269ii=521shoushu=221ii=522shoushu=230ii=523shoushu=214ii=524shoushu=225ii=525shoushu=240ii=526shoushu=242ii=527shoushu=217ii=528shoushu=231ii=529shoushu=237ii=530shoushu=212ii=531shoushu=292ii=532shoushu=234ii=533shoushu=223ii=534shoushu=275ii=535shoushu=174ii=536shoushu=262ii=537shoushu=249ii=538shoushu=271ii=539shoushu=217ii=540shoushu=256ii=541shoushu=201ii=542shoushu=213ii=543shoushu=182ii=544shoushu=222ii=545shoushu=276ii=546shoushu=293ii=547shoushu=220ii=548shoushu=226ii=549shoushu=233ii=550shoushu=221ii=551shoushu=282ii=552shoushu=189ii=553shoushu=275ii=554shoushu=196ii=555shoushu=243ii=556shoushu=281ii=557shoushu=196ii=558shoushu=191ii=559shoushu=246ii=560shoushu=207ii=561shoushu=255ii=562shoushu=291ii=563shoushu=249ii=564shoushu=239ii=565shoushu=189ii=566shoushu=269ii=567shoushu=210ii=568shoushu=221ii=569shoushu=178ii=570shoushu=218ii=571shoushu=260ii=572shoushu=173ii=573shoushu=259ii=574shoushu=242ii=575shoushu=201ii=576shoushu=204ii=577shoushu=271ii=578shoushu=174ii=579shoushu=241ii=580shoushu=176ii=581shoushu=175ii=582shoushu=191ii=583shoushu=192ii=584shoushu=249ii=585shoushu=258ii=586shoushu=218ii=587shoushu=219ii=588shoushu=191ii=589shoushu=258ii=590shoushu=186ii=591shoushu=237ii=592shoushu=255ii=593shoushu=292ii=594shoushu=258ii=595shoushu=263ii=596shoushu=189ii=597shoushu=245ii=598shoushu=253ii=599shoushu=242ii=600shoushu=197ii=601shoushu=200ii=602shoushu=191ii=603shoushu=281ii=604shoushu=229ii=605shoushu=206ii=606shoushu=250ii=607shoushu=243ii=608shoushu=234ii=609shoushu=252ii=610shoushu=207ii=611shoushu=255ii=612shoushu=220ii=613shoushu=267ii=614shoushu=264ii=615shoushu=267ii=616shoushu=270ii=617shoushu=267ii=618shoushu=238ii=619shoushu=260ii=620shoushu=225ii=621shoushu=284ii=622shoushu=287ii=623shoushu=232ii=624shoushu=275ii=625shoushu=254ii=626shoushu=202ii=627shoushu=227ii=628shoushu=265ii=629shoushu=259ii=630shoushu=280ii=631shoushu=256ii=632shoushu=247ii=633shoushu=240ii=634shoushu=187ii=635shoushu=268ii=636shoushu=228ii=637shoushu=252ii=638shoushu=280ii=639shoushu=209ii=640shoushu=245ii=641shoushu=259ii=642shoushu=233ii=643shoushu=268ii=644shoushu=233ii=645shoushu=297ii=646shoushu=185ii=647shoushu=266ii=648shoushu=187ii=649shoushu=177ii=650shoushu=231ii=651shoushu=218ii=652shoushu=186ii=653shoushu=183ii=654shoushu=283ii=655shoushu=244ii=656shoushu=288ii=657shoushu=218ii=658shoushu=258ii=659shoushu=189ii=660shoushu=198ii=661shoushu=224ii=662shoushu=177ii=663shoushu=274ii=664shoushu=281ii=665shoushu=236ii=666shoushu=171ii=667shoushu=193ii=668shoushu=239ii=669shoushu=232ii=670shoushu=248ii=671shoushu=258ii=672shoushu=188ii=673shoushu=196ii=674shoushu=262ii=675shoushu=222ii=676shoushu=263ii=677shoushu=270ii=678shoushu=211ii=679shoushu=223ii=680shoushu=253ii=681shoushu=213ii=682shoushu=308ii=683shoushu=236ii=684shoushu=174ii=685shoushu=179ii=686shoushu=220ii=687shoushu=209ii=688shoushu=161ii=689shoushu=236ii=690shoushu=174ii=691shoushu=204ii=692shoushu=212ii=693shoushu=180ii=694shoushu=290ii=695shoushu=260ii=696shoushu=192ii=697shoushu=295ii=698shoushu=272ii=699shoushu=198ii=700shoushu=252ii=701shoushu=172ii=702shoushu=264ii=703shoushu=251ii=704shoushu=270ii=705shoushu=218ii=706shoushu=244ii=707shoushu=272ii=708shoushu=252ii=709shoushu=204ii=710shoushu=198ii=711shoushu=271ii=712shoushu=261ii=713shoushu=291ii=714shoushu=171ii=715shoushu=235ii=716shoushu=246ii=717shoushu=272ii=718shoushu=245ii=719shoushu=188ii=720shoushu=320ii=721shoushu=175ii=722shoushu=363ii=723shoushu=207ii=724shoushu=244ii=725shoushu=262ii=726shoushu=293ii=727shoushu=262ii=728shoushu=250ii=729shoushu=195ii=730shoushu=203ii=731shoushu=178ii=732shoushu=208ii=733shoushu=185ii=734shoushu=205ii=735shoushu=247ii=736shoushu=255ii=737shoushu=208ii=738shoushu=249ii=739shoushu=285ii=740shoushu=177ii=741shoushu=203ii=742shoushu=252ii=743shoushu=184ii=744shoushu=214ii=745shoushu=251ii=746shoushu=283ii=747shoushu=211ii=748shoushu=191ii=749shoushu=250ii=750shoushu=258ii=751shoushu=207ii=752shoushu=285ii=753shoushu=251ii=754shoushu=290ii=755shoushu=274ii=756shoushu=247ii=757shoushu=198ii=758shoushu=205ii=759shoushu=262ii=760shoushu=212ii=761shoushu=253ii=762shoushu=277ii=763shoushu=276ii=764shoushu=241ii=765shoushu=219ii=766shoushu=187ii=767shoushu=265ii=768shoushu=253ii=769shoushu=213ii=770shoushu=230ii=771shoushu=234ii=772shoushu=209ii=773shoushu=284ii=774shoushu=185ii=775shoushu=248ii=776shoushu=227ii=777shoushu=205ii=778shoushu=294ii=779shoushu=255ii=780shoushu=258ii=781shoushu=288ii=782shoushu=220ii=783shoushu=214ii=784shoushu=208ii=785shoushu=222ii=786shoushu=266ii=787shoushu=224ii=788shoushu=191ii=789shoushu=237ii=790shoushu=257ii=791shoushu=241ii=792shoushu=261ii=793shoushu=268ii=794shoushu=232ii=795shoushu=197ii=796shoushu=231ii=797shoushu=214ii=798shoushu=228ii=799shoushu=259ii=800shoushu=260ii=801shoushu=266ii=802shoushu=225ii=803shoushu=142ii=804shoushu=202ii=805shoushu=265ii=806shoushu=187ii=807shoushu=285ii=808shoushu=198ii=809shoushu=199ii=810shoushu=254ii=811shoushu=248ii=812shoushu=281ii=813shoushu=216ii=814shoushu=224ii=815shoushu=180ii=816shoushu=295ii=817shoushu=271ii=818shoushu=206ii=819shoushu=173ii=820shoushu=148ii=821shoushu=243ii=822shoushu=198ii=823shoushu=175ii=824shoushu=283ii=825shoushu=273ii=826shoushu=203ii=827shoushu=178ii=828shoushu=173ii=829shoushu=183ii=830shoushu=199ii=831shoushu=218ii=832shoushu=268ii=833shoushu=174ii=834shoushu=224ii=835shoushu=237ii=836shoushu=204ii=837shoushu=228ii=838shoushu=243ii=839shoushu=203ii=840shoushu=181ii=841shoushu=261ii=842shoushu=225ii=843shoushu=153ii=844shoushu=224ii=845shoushu=244ii=846shoushu=159ii=847shoushu=289ii=848shoushu=148ii=849shoushu=175ii=850shoushu=225ii=851shoushu=241ii=852shoushu=156ii=853shoushu=160ii=854shoushu=285ii=855shoushu=183ii=856shoushu=169ii=857shoushu=224ii=858shoushu=188ii=859shoushu=238ii=860shoushu=272ii=861shoushu=203ii=862shoushu=233ii=863shoushu=193ii=864shoushu=274ii=865shoushu=185ii=866shoushu=217ii=867shoushu=151ii=868shoushu=206ii=869shoushu=211ii=870shoushu=267ii=871shoushu=296ii=872shoushu=193ii=873shoushu=255ii=874shoushu=182ii=875shoushu=225ii=876shoushu=217ii=877shoushu=212ii=878shoushu=167ii=879shoushu=150ii=880shoushu=250ii=881shoushu=267ii=882shoushu=205ii=883shoushu=193ii=884shoushu=276ii=885shoushu=226ii=886shoushu=206ii=887shoushu=228ii=888shoushu=151ii=889shoushu=146ii=890shoushu=134ii=891shoushu=227ii=892shoushu=183ii=893shoushu=180ii=894shoushu=264ii=895shoushu=202ii=896shoushu=183ii=897shoushu=233ii=898shoushu=173ii=899shoushu=163ii=900shoushu=216ii=901shoushu=169ii=902shoushu=279ii=903shoushu=155ii=904shoushu=239ii=905shoushu=274ii=906shoushu=156ii=907shoushu=214ii=908shoushu=158ii=909shoushu=180ii=910shoushu=213ii=911shoushu=247ii=912shoushu=278ii=913shoushu=226ii=914shoushu=330ii=915shoushu=269ii=916shoushu=236ii=917shoushu=166ii=918shoushu=276ii=919shoushu=202ii=920shoushu=280ii=921shoushu=307ii=922shoushu=169ii=923shoushu=183ii=924shoushu=265ii=925shoushu=209ii=926shoushu=239ii=927shoushu=226ii=928shoushu=216ii=929shoushu=152ii=930shoushu=253ii=931shoushu=235ii=932shoushu=271ii=933shoushu=182ii=934shoushu=271ii=935shoushu=258ii=936shoushu=189ii=937shoushu=238ii=938shoushu=243ii=939shoushu=205ii=940shoushu=224ii=941shoushu=253ii=942shoushu=244ii=943shoushu=225ii=944shoushu=209ii=945shoushu=231ii=946shoushu=164ii=947shoushu=211ii=948shoushu=165ii=949shoushu=276ii=950shoushu=271ii=951shoushu=233ii=952shoushu=258ii=953shoushu=267ii=954shoushu=205ii=955shoushu=256ii=956shoushu=212ii=957shoushu=222ii=958shoushu=172ii=959shoushu=270ii=960shoushu=258ii=961shoushu=259ii=962shoushu=219ii=963shoushu=243ii=964shoushu=233ii=965shoushu=264ii=966shoushu=235ii=967shoushu=215ii=968shoushu=272ii=969shoushu=211ii=970shoushu=281ii=971shoushu=186ii=972shoushu=242ii=973shoushu=219ii=974shoushu=243ii=975shoushu=258ii=976shoushu=166ii=977shoushu=250ii=978shoushu=245ii=979shoushu=169ii=980shoushu=214ii=981shoushu=311ii=982shoushu=227ii=983shoushu=265ii=984shoushu=256ii=985shoushu=228ii=986shoushu=228ii=987shoushu=223ii=988shoushu=189ii=989shoushu=329ii=990shoushu=280ii=991shoushu=194ii=992shoushu=194ii=993shoushu=263ii=994shoushu=183ii=995shoushu=246ii=996shoushu=240ii=997shoushu=205ii=998shoushu=261ii=999shoushu=338finally
		// ii=85shoushu=234
		// ii=88shoushu=259
		// ii=89shoushu=213
		tempLength[0] = 309 * 2;
		tempLength[85] = 234 * 2;
		tempLength[88] = 259 * 2;
		tempLength[89] = 213 * 2;
		for (java.util.Iterator<GMDGoManual> iter = list.iterator(); iter.hasNext();) {
			byte[] temp = (byte[]) iter.next().getMoves();
			// assertEquals(1000,temp.length);
		}
		// log.debug(list);
	}

	public void testLoadOneFromAllGoManual() {
		byte[] temp = new LoadGMDGoManual(rootDir).loadSingleGoManual()
				.getMoves();
		log.debug(temp.toString());
		byte[] temp1 = new LoadGMDGoManual(rootDir)
				.loadOneFromAllGoManual(0, 0);
		assertEquals(temp.length, temp1.length);
		for (int i = 0; i < temp.length; i++) {
			assertEquals(temp[i], temp1[i]);
		}
	}

	/**
	 * GMDGoManual [ id=6356, moves=2, blackName=陈临新, whiteName=王伯刚]<br/>
	 * GMDGoManual [ id=5490, moves=2, blackName=刘小光, whiteName=钱宇平]<br/>
	 * GMDGoManual [ id=6054, moves=1, blackName=淡路修三, whiteName=刘小光]<br>
	 * A few new manual has some problem.
	 */
	public void testLoadAllGoManual() {
		List<GMDGoManual> manuals = GMDGoManual.loadMultiGoManual(
				Constant.GLOBAL_MANUAL, 6391);
		int maxMove = 0;
		GMDGoManual maxMoveManual = null;
		GMDGoManual minMoveManual = null;
		int minMove = 512;
		List<GMDGoManual> list = new ArrayList<GMDGoManual>();
		for (GMDGoManual manual : manuals) {

			int moves = manual.getSteps().size();
			if (moves > maxMove) {
				maxMove = moves;
				maxMoveManual = manual;
			}
			if (moves < minMove) {
				minMove = moves;
				minMoveManual = manual;
			}
			if (moves < MOVES_THRESHOLD) {
				list.add(manual);
			}
		}
		if(log.isDebugEnabled()) log.debug("maxMove=" + maxMove);
		if(log.isDebugEnabled()) log.debug("maxMoveManual=" + maxMoveManual);
		if(log.isDebugEnabled()) log.debug("minMoveManual=" + minMoveManual);
		if(log.isDebugEnabled()) log.debug("manual less than " + MOVES_THRESHOLD + " moves:");
		for (GMDGoManual manual : list) {
			if(log.isDebugEnabled()) log.debug(manual);
		}
	}
}
