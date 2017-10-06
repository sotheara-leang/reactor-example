package com.example.reactor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MyExampleTest {

	@Test
	public void example1() {
		Flux<String> seq1 = Flux.just("foo", "bar", "foobar");
		seq1.subscribe(System.out::println);

		List<String> iterable = Arrays.asList("foo2", "bar2", "foobar2");
		Flux<String> seq2 = Flux.fromIterable(iterable);
		seq2.subscribe(System.out::println);
	}

	@Test
	public void example2() {
		Mono<String> noData = Mono.empty();
		noData.subscribe(System.out::println);

		Mono<String> data = Mono.just("foo");
		data.subscribe(System.out::println);

		Flux<Integer> numbersFromFiveToSeven = Flux.range(5, 3);
		numbersFromFiveToSeven.subscribe(System.out::println);
	}

	@Test
	public void example3() {
		Flux<Integer> ints = Flux.range(1, 3);
		ints.subscribe(i -> System.out.println(i));
	}

	@Test
	public void example4() {
		Flux<Integer> ints = Flux.range(1, 4).map(i -> {
			if (i <= 3)
				return i;
			throw new RuntimeException("Got to 4");
		});
		ints.subscribe(i -> System.out.println(i), error -> System.err.println("Error: " + error));
	}

	@Test
	public void example5() {
		Flux<Integer> ints = Flux.range(1, 4);
		ints.subscribe(i -> System.out.println(i), error -> System.err.println("Error " + error), () -> {
			System.out.println("Done");
		});
	}

	@Test
	public void example6() {
		SampleSubscriber<Integer> ss = new SampleSubscriber<Integer>();
		Flux<Integer> ints = Flux.range(1, 4);
		ints.subscribe(i -> System.out.println(i), error -> System.err.println("Error " + error), () -> {
			System.out.println("Done");
		}, s -> ss.request(10));
		ints.subscribe(ss);
	}

	@Test
	public void example7() {
		Flux<String> flux = Flux.generate(() -> 0, (state, sink) -> {
			sink.next("3 x " + state + " = " + 3 * state + "\n");
			if (state == 10) {
				sink.complete();
			}
			return state + 1;
		});
		flux.subscribe(System.out::print);
	}

	@Test
	public void example8() {
		Flux<String> flux = Flux.generate(AtomicLong::new, (state, sink) -> {
			long i = state.getAndIncrement();
			sink.next("3 x " + i + " = " + 3 * i + "\n");
			if (i == 10) {
				sink.complete();
			}
			return state;
		});
		flux.subscribe(System.out::print);
	}

	@Test
	public void example9() {
		Flux<String> flux = Flux.generate(AtomicLong::new, (state, sink) -> {
			long i = state.getAndIncrement();
			sink.next("3 x " + i + " = " + 3 * i);
			if (i == 10) {
				sink.complete();
			}
			return state;
		}, (state) -> System.out.println("state: " + state));
		flux.subscribe();
	}
}
