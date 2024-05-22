build-app:
	@docker build --target app -t capital-gain-app .

run:
	@docker run -i capital-gain-app <&0

build-test:
	@docker build --target test -t capital-gain-test .

test:
	@docker run capital-gain-test

lint:
	@docker run -v $(PWD)/src:/src -v $(PWD)/test:/test --rm cljkondo/clj-kondo clj-kondo --lint src test


.PHONY: build-app run build-test test lint
