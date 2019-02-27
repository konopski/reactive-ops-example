package eu.konopski.reactive.ops;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;

import com.google.common.collect.Lists;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class Main
{
	static class DataManager {

		List<Integer> numbers = Lists.newArrayList(2,3,4,5,6,7,8,9,10);
		Executor jobExecutor = Executors.newFixedThreadPool(2);

		Observable<Integer> getNumbers() {
			return Observable.fromIterable(numbers);
		}

		List<Integer> getNumbersSynch() {
			return numbers;
		}

		Observable<Integer> squareOf(int number) {
			return Observable.just(number * number).subscribeOn(Schedulers.from(jobExecutor));
		}
	}

	final static DataManager data = new DataManager();

	final static Function<Integer, Observable<Integer>> SQUARE = data::squareOf;

	public static void main(String[] args) {

		data.getNumbers().flatMap(SQUARE::apply).take(10).subscribe(x -> System.out.println("x = " + x));

//		x = 9
//		x = 16
//		x = 25
//		x = 36
//		x = 49
//		x = 64
//		x = 81
//		x = 100
//		x = 4

		data.getNumbers().concatMap(SQUARE::apply).take(10).subscribe(y -> System.out.println("y = " + y));

//		y = 4
//		y = 9
//		y = 16
//		y = 25
//		y = 36
//		y = 49
//		y = 64
//		y = 81
//		y = 100


		data.getNumbers().switchMap(SQUARE::apply).take(10).subscribe(z -> System.out.println("z = " + z));
//		z = 100



	}
}
