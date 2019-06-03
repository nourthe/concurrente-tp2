class PNtest {
	PN pene = new PN();

	public PNtest() {
		if (pene.isTransitionEnabled(PN.Transitions.PRODUCE_BUFFER_1)) {
			System.out.println("Puedo producir buffer 1");
			pene.fire(PN.Transitions.PRODUCE_BUFFER_1);
			pene.fire(PN.Transitions.FINISHED_PRODUCING_BUFFER_1);
		}
		if (pene.isTransitionEnabled(PN.Transitions.PRODUCE_BUFFER_2)) {
			System.out.println("Puedo producir buffer 2");
			pene.fire(PN.Transitions.PRODUCE_BUFFER_2);
			pene.fire(PN.Transitions.FINISHED_PRODUCING_BUFFER_2);
		}
		if (pene.isTransitionEnabled(PN.Transitions.CONSUME_BUFFER_1)) {
			System.out.println("Puedo consumir buffer 1");
			pene.fire(PN.Transitions.CONSUME_BUFFER_1);
			pene.fire(PN.Transitions.FINISHED_CONSUMING_BUFFER_1);
		}
		if (pene.isTransitionEnabled(PN.Transitions.CONSUME_BUFFER_2)) {
			System.out.println("Puedo consumir buffer 2");
			pene.fire(PN.Transitions.CONSUME_BUFFER_2);
			pene.fire(PN.Transitions.FINISHED_CONSUMING_BUFFER_2);
		}
		if (!pene.isTransitionEnabled(PN.Transitions.CONSUME_BUFFER_1)) {
			System.out.println("No se puede consumir en buffer 1");
		}
		if (!pene.isTransitionEnabled(PN.Transitions.CONSUME_BUFFER_2)) {
			System.out.println("No se puede consumir en buffer 2");
		}

		for (int i = 0; i<20; i++) {
			if (pene.isTransitionEnabled(PN.Transitions.PRODUCE_BUFFER_1)) {
				System.out.println("Puedo producir buffer 1 numero: " + i);
				pene.fire(PN.Transitions.PRODUCE_BUFFER_1);
				pene.fire(PN.Transitions.FINISHED_PRODUCING_BUFFER_1);
			}
			else {
				System.out.println("No puedo producir en buffer 1 numero: " + i);
			}
			if (pene.isTransitionEnabled(PN.Transitions.PRODUCE_BUFFER_2)) {
				System.out.println("Puedo producir buffer 2 numero: " + i);
				pene.fire(PN.Transitions.PRODUCE_BUFFER_2);
				pene.fire(PN.Transitions.FINISHED_PRODUCING_BUFFER_2);
			}
			else {
				System.out.println("No puedo producir en buffer 2 numero: " + i);
			}
		}
	}
}
