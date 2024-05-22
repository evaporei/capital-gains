build-app:
	@docker build --target app -t capital-gain-app .

run:
	@docker run -i capital-gain-app <&0

build-test:
	@docker build --target test -t capital-gain-test .

test:
	@docker run capital-gain-test

lint:
	@docker run -v $(shell pwd)/src:/src -v $(shell pwd)/test:/test --rm borkdude/clj-kondo clj-kondo --lint src test


.PHONY: build-app run build-test test lint
