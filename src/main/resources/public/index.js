class ToDo {
    constructor(id, title, content, completed) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.completed = completed;
	}
}

const {el, list, mount } = redom;

class Li {
	constructor() {
		this.el = el('li.todo');
	}
	update(todo) {
		this.el.textContent = JSON.stringify(todo);
	}
}
const ul = list('ul#todos', Li);
mount(document.body, ul);

fetch('/all')
.then(r => r.json())
.then(r => ul.update(r));