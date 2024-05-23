build-app:
	@docker build --target app -t capital-gains-app .

run:
	@docker run -i capital-gains-app <&0

build-test:
	@docker build --target test -t capital-gains-test .

test:
	@docker run capital-gains-test

lint:
	@docker run -v $(PWD)/src:/src -v $(PWD)/test:/test --rm cljkondo/clj-kondo clj-kondo --lint src test


.PHONY: build-app run build-test test lint
